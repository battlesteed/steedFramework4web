package steed.largewebsite.ogm;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import steed.domain.BaseDomain;
import steed.domain.BaseUnionKeyDomain;
import steed.domain.annotation.NotQueryCondition;
import steed.domain.annotation.UpdateEvenNull;
import steed.largewebsite.ogm.domain.BaseNosqlDomain;
import steed.util.base.BaseUtil;
import steed.util.base.DomainUtil;
import steed.util.base.StringUtil;
import steed.util.reflect.ReflectUtil;

public class NoSqlDao {
	private static final Logger logger = LoggerFactory.getLogger(NoSqlDao.class);
	
	private static final ThreadLocal<Boolean> transactionType = new ThreadLocal<>();
	// 具体的错误提示用
	private static final ThreadLocal<Exception> exception = new ThreadLocal<>();
	private static final ThreadLocal<Transaction> currentTransaction = new ThreadLocal<>();
	/**
	 * 是否自动提交或回滚事务
	 * 自助事务步骤：
	 * 1,调用setAutoManagTransaction(false)把自动事务设为false
	 * 2,调用managTransaction()管理事务;
	 */
	private static final ThreadLocal<Boolean> autoManagTransaction = new ThreadLocal<>();
	
	private NoSqlDao() {
	}
	
	/***********#异常提示专用************/
	public static Exception getExceptiontype() {
		return exception.get();
	}
	public static void setException(Exception exception) {
		NoSqlDao.exception.set(exception);
		logger.error("数据库操作发生异常",exception);
	}
	public static Transaction getCurrentTransaction() {
		return currentTransaction.get();
	}
	public static void setCurrentTransaction(Transaction currentTransaction) {
		NoSqlDao.currentTransaction.set(currentTransaction);
	}
	public static Boolean getTransactionType() {
		return transactionType.get();
	}
	public static void setTransactionType(Boolean transactionType) {
		NoSqlDao.transactionType.set(transactionType);
	}
	public static Boolean getAutoManagTransaction() {
		return autoManagTransaction.get();
	}
	/**
	 * 立即事务开始，框架可能配置了多个数据库操作使用同一事务然后统一提交
	 * 如某些操作可能要马上提交事务，可使用该方法
	 * 用法<br />
	 *  ImmediatelyTransactionData immediatelyTransactionData = NoSqlDao.immediatelyTransactionBegin();<br />
	 *  //TODO 这里做其他数据库操作<br />
	 *	NoSqlDao.immediatelyTransactionEnd(immediatelyTransactionData);<br />
	 *	
	 *	@see #immediatelyTransactionEnd
	 * @return 
	 */
	public static ImmediatelyTransactionData immediatelyTransactionBegin(){
		Transaction currentTransaction = getCurrentTransaction();
		Boolean autoManagTransaction = getAutoManagTransaction();
		setAutoManagTransaction(true);
		setCurrentTransaction(null);
		return new ImmediatelyTransactionData(currentTransaction, autoManagTransaction);
	}
	/**
	 * 立即事务结束
	 * @see #immediatelyTransactionBegin
	 * @param immediatelyTransactionData
	 */
	public static void immediatelyTransactionEnd(ImmediatelyTransactionData immediatelyTransactionData){
		NoSqlDao.setCurrentTransaction(immediatelyTransactionData.currentTransaction);
		NoSqlDao.setAutoManagTransaction(immediatelyTransactionData.autoManagTransaction);
	}
	
	
	
	
	public static void setAutoManagTransaction(Boolean selfManagTransaction) {
		//logger.debug("自动事务----->"+selfManagTransaction);
		NoSqlDao.autoManagTransaction.set(selfManagTransaction);;
	}
	/***************************增删查改开始******************************/
	
	
	public static boolean saveList(List<? extends BaseNosqlDomain> list){
		Session session = null;
		try {
			session = OgmUtil.getSession();
			beginTransaction();
			for (Object obj:list) {
				session.save(obj);
			}
			return managTransaction(true);
		} catch (Exception e) {
			setException(e);
			e.printStackTrace();
			return managTransaction(false);
		}finally{
			closeSession();
		}
	}
	
	
	
	/**
	 * list所有对象一起update成功或失败,update成功或失败的单位是整个list
	 * ,你可能需要另外一个方法updateListOneByOne(List list)
	 * @see #updateListOneByOne
	 * @param list
	 * @return
	 */
	public static boolean updateList(List<? extends BaseNosqlDomain> list){
		Session session = null;
		try {
			session = OgmUtil.getSession();
			beginTransaction();
			for (Object obj:list ) {
				session.update(obj);
			}
			return managTransaction(true);
		} catch (Exception e) {
			e.printStackTrace();
			setException(e);
			return managTransaction(false);
		}finally{
			closeSession();
		}
	}
	
	/**
	 * 不要求list里面所有对象一起update成功或失败,update单位是单个对象,
	 * 你可能需要另外一个方法updateList(List list),请参看本类中的updateEvenNull方法
	 * @param list
	 * @param updateEvenNull 即使为空也更新到数据库中的字段，如果为null，
	 * 			则根据domain字段中的UpdateEvenNull注解更新，
	 * 
	 * @return update失败的对象数
	 */
	public static int updateListNotNullFieldOneByOne(List<? extends BaseNosqlDomain> list,List<String> updateEvenNull){
		int failed = 0;
		for (Object o:list) {
			if (!updateNotNullField((BaseNosqlDomain) o, updateEvenNull)) {
				failed++;
			}
		}
		return failed;
	}
	
	public static void evict(Object obj){
		OgmUtil.getSession().evict(obj);
		closeSession();
	}
	/**
	 * 如果数据库有obj对象就update否则save
	 * @param obj
	 * @return
	 */
	public static boolean saveOrUpdate(BaseNosqlDomain obj){
		Session session = null;
		try {
			session = OgmUtil.getSession();
			beginTransaction();
			if (BaseUtil.isObjEmpty(DomainUtil.getDomainId(obj))) {
				return save(obj);
			}else {
				BaseNosqlDomain smartGet = smartGet(obj);
				if (smartGet != null) {
					session.evict(smartGet);
//					session.clear();
					session.update(obj);
				}else {
					session.save(obj);
				}
			}
			return managTransaction(true);
		} catch (Exception e) {
			e.printStackTrace();
			setException(e);
			return managTransaction(false);
		}finally{
			closeSession();
		}
	}
	
	/**
	 * 级联删除,不推荐,应该重写实体类delete方法实现级联删除
	 * @param obj
	 * @return
	 */
	@Deprecated
	public static boolean cascadeDelete(BaseNosqlDomain obj,List<Class<?>> domainSkip){
		beginTransaction();
		if (domainSkip == null) {
			domainSkip = new ArrayList<Class<?>>();
		}
		boolean delete = deleteConneced(obj,Integer.MAX_VALUE,domainSkip);
		managTransaction(delete);
		return delete;
	}
	
	
	/**
	 * 删除obj对应的数据库记录
	 * @param obj
	 * @return
	 */
	public static boolean delete(BaseNosqlDomain obj){
		Session session = null;
		try {
			session = OgmUtil.getSession();
			beginTransaction();
			session.delete(obj);
			return managTransaction(true);
		} catch(NonUniqueObjectException e1){
			try {
				session.delete(smartGet(obj));
				return managTransaction(true);
			} catch (Exception e) {
				e.printStackTrace();
				setException(e);
				return managTransaction(false);
			}finally{
				closeSession();
			}
		}catch (Exception e) {
			e.printStackTrace();
			setException(e);
			return managTransaction(false);
		}finally{
			closeSession();
		}
	}
	
	private static boolean deleteConneced(BaseNosqlDomain obj,int level,List<Class<?>> domainSkip){
		if (level-- == 0) {
			return true;
		}
		Session session = null;
		try {
			session = OgmUtil.getSession();
			BaseNosqlDomain smartGet = NoSqlDao.smartGet(obj);
			if (smartGet == null) {
				return true;
			}
			for (Field temp : smartGet.getClass().getDeclaredFields()) {
				try {
					Class<?> type = temp.getType();
					if (!domainSkip.contains(temp)) {
						if (BaseNosqlDomain.class.isAssignableFrom(type)) {
							//TODO 支持javax.persistence.OneToOne外的其他OneToOne注解
							if (ReflectUtil.getAnnotation(OneToOne.class, smartGet.getClass(), temp) != null) {
								temp.setAccessible(true);
								BaseNosqlDomain object = (BaseNosqlDomain) temp.get(smartGet);
								if (!BaseUtil.isObjEmpty(object)) {
									if (deleteConneced(object,level,domainSkip)) {
										session.delete(object);
									} else {
										return false;
									}
								}
							}
						} else if (Collection.class.isAssignableFrom(type)) {
							if (ReflectUtil.getAnnotation(OneToMany.class, smartGet.getClass(), temp) != null) {
								temp.setAccessible(true);
								Collection<?> collection = (Collection<?>) temp.get(smartGet);
								// 获取Collection泛型，看是不是BaseNosqlDomain，然后循环删除
								try {
									Class<?> c = ReflectUtil.getGenericType(temp);
									if (BaseNosqlDomain.class.isAssignableFrom(c)) {
										for(Object o:collection){
											if(!deleteConneced((BaseNosqlDomain)o, level, domainSkip)){
												return false;
											}
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			session.delete(obj);
			return true;
		} catch (NonUniqueObjectException e1) {
			try {
				session.delete(smartGet(obj));
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				setException(e);
				return false;
			} finally {
				closeSession();
			}
		} catch (Exception e) {
			e.printStackTrace();
			setException(e);
			return false;
		} finally {
			closeSession();
		}
	}
	
	/*private static boolean isContainReferenceAnnotation(Field f,Class<?> clazz){
		List<Annotation> annotations = ReflectUtil.getAnnotations(clazz, f);
		if (annotations.contains(OneToOne.class)) {
			return true;
		}else if(annotations.contains(on.class)){
			
		}
	}*/
	
	
	@SuppressWarnings("unchecked")
	public static <T extends BaseNosqlDomain> T smartLoad(T domain){
		return (T) load(domain.getClass(), DomainUtil.getDomainId(domain));
	}
	/**
	 * 聪明的get方法
	 */
	@SuppressWarnings("unchecked")
	public static <T extends BaseNosqlDomain> T smartGet(T domain){
		return (T) get(domain.getClass(), DomainUtil.getDomainId(domain));
	}
	
	public static <T extends BaseNosqlDomain> T get(Class<T> t,Serializable key){
		try {
			T t2 = (T) getSession().get(t, key);
			return t2;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			closeSession();
		}
	}
	
	public static <T extends BaseNosqlDomain> List<T> getList(Class<T> t,Serializable[] keys){
		try {
			List<T> list = new ArrayList<T>();
			for (Serializable s:keys) {
				list.add(get(t, s));
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			closeSession();
		}
	}
	
	
	
	public static <T extends BaseNosqlDomain> T load(Class<T> t,Serializable key){
		try {
			Session session = OgmUtil.getSession();
			return (T) session.load(t, key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			closeSession();
		}
	}
	/**
	 *  管理事务
	 * @param isCommit 
	 * @return 
	 */
	private static boolean managTransaction(Boolean isCommit){
		if (autoManagTransaction.get() == null || autoManagTransaction.get() == true) {
			if (isCommit) {
				commitTransaction();
			}else if(!isCommit){
				rollbackTransaction();
			}
			return isCommit;
		}else{
			Boolean type = transactionType.get();
			if (type == null) {
				transactionType.set(isCommit);
			}else{
				transactionType.set(type&&isCommit);
			}
			return transactionType.get();
		}
	}
	
	public static void beginTransaction(){
		if (currentTransaction.get() == null) {
			currentTransaction.set(getSession().beginTransaction());
		}
	}
	public static Transaction getTransaction(){
		return currentTransaction.get();
	}
	
	/***
	 * 管理事务
	 * @return 事务是成功提交还是回滚
	 */
	public static boolean managTransaction(){
		Boolean boolean1 = transactionType.get();
		try {
			if (boolean1 == null) {
				return true;
			}
			if (boolean1) {
				commitTransaction();
			}else if(!boolean1){
				rollbackTransaction();
			}
			return boolean1;
		} catch (Exception e) {
			e.printStackTrace();
			rollbackTransaction();
			setException(e);
			return false;
		}
	}
	
	/**
	 * 释放资源
	 */
	public static void relese(){
		transactionType.remove();
		autoManagTransaction.remove();
		currentTransaction.remove();
		exception.remove();
	}
	
	
	public static void commitTransaction(){
		Transaction transaction = getTransaction();
		if (transaction != null) {
			transaction.commit();
		}
		currentTransaction.remove();
	}
	
	public static void rollbackTransaction(){
		Transaction transaction = getTransaction();
		if (transaction != null) {
			transaction.rollback();
		}
		currentTransaction.remove();
	}
	
	public static boolean save(BaseNosqlDomain obj){
		try {
			Session session = OgmUtil.getSession();
			beginTransaction();
			session.save(obj);
			return managTransaction(true);
		} catch (Exception e) {
			e.printStackTrace();
			setException(e);
			return managTransaction(false);
		}finally{
			closeSession();
		}
	}
	/**
	 * update实体类中不为空的字段
	 * @param obj
	 * @param updateEvenNull 即使为空也更新到数据库中的字段，如果为null，
	 * 			则根据domain字段中的UpdateEvenNull注解进行更新，
	 * 			所以想跳过UpdateEvenNull注解只更新不为空的字段可以传一个空的list
	 * @return
	 */
	public static boolean updateNotNullField(BaseNosqlDomain obj,List<String> updateEvenNull){
		return updateNotNullField(obj, updateEvenNull, false);
	}
	/**
	 * update实体类中不为空的字段
	 * @param obj
	 * @param updateEvenNull 即使为空也更新到数据库中的字段，如果为null，
	 * 			则根据domain字段中的UpdateEvenNull注解进行更新，
	 * 			所以想跳过UpdateEvenNull注解只更新不为空的字段可以传一个空的list
	 * @param strictlyMode 严格模式，如果为true则 字段==null才算空，
	 * 	否则调用BaseUtil.isObjEmpty判断字段是否为空
	 * @see BaseUtil#isObjEmpty
	 * @see steed.util.base.DomainUtil#fillDomain
	 * @return
	 */
	public static boolean updateNotNullField(BaseNosqlDomain obj,List<String> updateEvenNull,boolean strictlyMode){
		List<String> list;
		if (updateEvenNull == null) {
			list = new ArrayList<String>();
			Field[] declaredFields = obj.getClass().getDeclaredFields();
			for (Field f:declaredFields) {
				if (f.getAnnotation(UpdateEvenNull.class) != null) {
					list.add(f.getName());
				}
			}
		}else {
			list = updateEvenNull;
		}
		return update(DomainUtil.fillDomain(smartGet(obj), obj,list,strictlyMode));
	}
	
	public static boolean update(BaseNosqlDomain obj){
		try {
			Session session = OgmUtil.getSession();
			beginTransaction();
			session.update(obj);
			return managTransaction(true);
		} catch (Exception e) {
			e.printStackTrace();
			return managTransaction(false);
		}finally{
			closeSession();
		}
	}
	
	/**
	 * 不要求list里面所有对象一起update成功或失败,update单位是单个对象,
	 * 你可能需要另外一个方法updateList(List list)
	 * @see #updateList
	 * @param list
	 * @return update失败的对象数
	 */
	public static int updateListOneByOne(List<? extends BaseNosqlDomain> list){
		int failed = 0;
		for (BaseNosqlDomain o:list) {
			if (!update(o)) {
				failed++;
			}
		}
		return failed;
	}
	/***************************增删查改结束******************************/
	
	
	
	
	/***************************数据库工具开始******************************/
	
	
	protected static Session getSession(){
		return OgmUtil.getSession();
	}
	/**
	 * 根据配置关闭session
	 */
	public static void closeSession(){
		if (OgmUtil.getColseSession() && (autoManagTransaction.get() == null || autoManagTransaction.get())) {
			OgmUtil.closeSession();
		}
	}
	/***************************数据库工具结束********************************/
	
	
	
	/***************************内部方法开始********************************/
	
	/**
	 *  把obj中非空字段放到map
	 * @param obj
	 * @param map
	 */
	public static void putField2Map(Object obj,Map<String, Object> map) {
		putField2Map(obj, map, "");
	}
	/**
	 * 把obj中非空字段放到map
	 * @param t
	 * @param obj
	 * @return
	 */
	private static void putField2Map(Object obj,Map<String, Object> map,String prefixName) {
		try {
			Class<? extends Object> objClass = obj.getClass();
			List<Field> Fields = ReflectUtil.getAllFields(obj);
			for (Field f:Fields) {
				String fieldName = f.getName();
				if ("serialVersionUID".equals(fieldName)) {
					continue;
				}
				//标有Transient且不是索引字段即跳过
/*					if (f.getAnnotation(Transient.class) != null) {
						continue;
					}
*/				if (ReflectUtil.getAnnotation(NotQueryCondition.class, objClass, f) != null) {
					continue;
				}
				String fieldGetterName = StringUtil.getFieldGetterName(fieldName);
				/*if (f.getType() != Boolean.class) {
					fieldGetterName = StringUtil.getFieldGetMethodName(fieldName);
				}else {
					fieldGetterName = StringUtil.getFieldIsMethodName(fieldName);
				}*/
				Method fidleMethod = ReflectUtil.getDeclaredMethod(objClass, fieldGetterName);
				if (fidleMethod != null) {
					if(fidleMethod.getAnnotation(Transient.class) != null){
						continue;
					}
				}
				
				f.setAccessible(true);
				Object value = f.get(obj);
				if (!BaseUtil.isObjEmpty(value)) {
					if (value instanceof BaseNosqlDomain 
							&& !(value instanceof BaseUnionKeyDomain)
							&& BaseUtil.isObjEmpty(DomainUtil.getDomainId((BaseDomain) value))) {
						//TODO 子查询支持
						putField2Map(value, map,fieldName +".");
					}else {
						if (value instanceof BaseNosqlDomain ) {
							JoinColumn annotation = ReflectUtil.getAnnotation(JoinColumn.class, objClass, f);
							if (annotation == null 
									|| !(!annotation.insertable() 
											&& !annotation.updatable())) {
								map.put(prefixName + fieldName, value);
							}
						}else {
							map.put(prefixName + fieldName, value);
						}
					}
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	
	/***************************内部方法结束********************************/

	public static class ImmediatelyTransactionData{
		Transaction currentTransaction;
		Boolean autoManagTransaction;
		public ImmediatelyTransactionData(Transaction currentTransaction,Boolean autoManagTransaction) {
			this.currentTransaction = currentTransaction;
			this.autoManagTransaction = autoManagTransaction;
		}
		
	}
}
