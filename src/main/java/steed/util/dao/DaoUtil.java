package steed.util.dao;


import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.NonUniqueObjectException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import steed.domain.BaseDomain;
import steed.domain.BaseRelationalDatabaseDomain;
import steed.domain.BaseUnionKeyDomain;
import steed.domain.annotation.NotQueryCondition;
import steed.domain.annotation.UpdateEvenNull;
import steed.domain.application.Page;
import steed.util.base.BaseUtil;
import steed.util.base.CollectionsUtil;
import steed.util.base.DomainUtil;
import steed.util.base.StringUtil;
import steed.util.reflect.ReflectUtil;
/**
 * 实现0sql和0hql伟大构想的dao工具类，用该类即可满足绝大多数数据库操作
_______________#########_______________________<br>
______________############_____________________<br>
______________#############____________________<br>
_____________##__###########___________________<br>
____________###__######_#####__________________<br>
____________###_#######___####_________________<br>
___________###__##########_####________________<br>
__________####__###########_####_______________<br>
________#####___###########__#####_____________<br>
_______######___###_########___#####___________<br>
______######___###___########___######_________<br>
_____#######___###__###########___######_______<br>
____#######___####_##############__######______<br>
___########__#####################_#######_____<br>
___########__##############################____<br>
___#######__######_#################_#######___<br>
___#######__######_######_#########___######___<br>
___#######____##__######___######_____######___<br>
___#######________######____#####_____#####____<br>
____######________#####_____#####_____####_____<br>
_____#####________####______#####_____###______<br>
______#####______;###________###______#________<br>
________##_______####________####______________ <br>
                                     	葱官赐福   百无禁忌
 * @author 战马
 */
public class DaoUtil {
	private static final Logger logger = LoggerFactory.getLogger(DaoUtil.class);
	
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
	
	/**
	 * 默认hql生成器,可以通过config.properties的dao.HqlGenerator配置
	 */
	private static HqlGenerator hqlGenerator = ReflectUtil.getInstanceFromProperties("dao.HqlGenerator");
	
	private DaoUtil() {
	}

	/**
	 * 查询条件后缀
	 */
	private static final String[] indexSuffix = {"_max_1","_min_1","_like_1","_not_in_1","_not_equal_1","_not_join","_not_null","_not_compile_param"};
	/***********\异常提示专用************/
	
	/*//TODO 完善异常类型
	private static final Integer[] exceptionTypes = {10,11};
	private static final String[] exceptionReasons = {"主键重复","主键未指定"};
	private static final Exception[] exceptions = {};*/
	
	/***********#异常提示专用************/
	public static Exception getExceptiontype() {
		return exception.get();
	}
	public static void setException(Exception exception) {
		DaoUtil.exception.set(exception);
		logger.error("数据库操作发生异常",exception);
	}
	public static Transaction getCurrentTransaction() {
		return currentTransaction.get();
	}
	public static void setCurrentTransaction(Transaction currentTransaction) {
		DaoUtil.currentTransaction.set(currentTransaction);
	}
	public static Boolean getTransactionType() {
		return transactionType.get();
	}
	public static void setTransactionType(Boolean transactionType) {
		DaoUtil.transactionType.set(transactionType);
	}
	public static Boolean getAutoManagTransaction() {
		return autoManagTransaction.get();
	}
	/**
	 * 立即事务开始，框架可能配置了多个数据库操作使用同一事务然后统一提交
	 * 如某些操作可能要马上提交事务，可使用该方法
	 * 用法<br />
	 *  ImmediatelyTransactionData immediatelyTransactionData = DaoUtil.immediatelyTransactionBegin();<br />
	 *  //TODO 这里做其他数据库操作<br />
	 *	DaoUtil.immediatelyTransactionEnd(immediatelyTransactionData);<br />
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
		DaoUtil.setCurrentTransaction(immediatelyTransactionData.currentTransaction);
		DaoUtil.setAutoManagTransaction(immediatelyTransactionData.autoManagTransaction);
	}
	
	
	
	
	public static void setAutoManagTransaction(Boolean selfManagTransaction) {
		DaoUtil.autoManagTransaction.set(selfManagTransaction);;
	}
	/***************************增删查改开始******************************/
	/**
	 */
	@SuppressWarnings("rawtypes")
	public static <T> Page<T> listObj(int pageSize,int currentPage, Class<? extends BaseRelationalDatabaseDomain> t){
		try {
			StringBuffer hql = getSelectHql(t,null,null,null);
			Long recordCount = getRecordCount(null, hql);
			Query query = getSession().createQuery(hql.toString());
			
			faging(pageSize,currentPage, query);
			List list = query.list();
			
			return setPage(currentPage, recordCount, pageSize, list);
		} catch (Exception e) {
			e.printStackTrace();
			setException(e);
			return null;
		}finally{
			closeSession();
		}
	}
	/**
	 * 查询t对应的表中所有记录的主键
	 * @param t
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<Serializable> listAllObjKey(Class<? extends BaseRelationalDatabaseDomain> t){
		try {
			String name = t.getName();
			String keyName = DomainUtil.getDomainIDName(t);
			String hql = "select "+keyName+" from " + name;
			Query query = getSession().createQuery(hql);
			return query.list();
		} catch (Exception e) {
			setException(e);
			e.printStackTrace();
			return null;
		}finally{
			closeSession();
		}
	}
	
	public static boolean saveList(List<? extends BaseRelationalDatabaseDomain> list){
		Session session = null;
		try {
			session = getSession();
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
	 * 查询所有id在ids里面的实体类
	 * @param t
	 * @param ids 实体类id，英文逗号分割
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> listByKeys(Class<? extends BaseRelationalDatabaseDomain> t,String[] ids){
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			Class<? extends Serializable> idClass = DomainUtil.getDomainIDClass(t);
			Serializable[] serializables;
			if (idClass == String.class) {
				serializables = ids;
			}else{
				serializables = new Serializable[ids.length];
				for (int i = 0; i < serializables.length; i++) {
					serializables[i] = (Serializable) ReflectUtil.string2BaseID(idClass, ids[i]);
				}
			}
			map.put(DomainUtil.getDomainIDName(t)+"_not_join", serializables);
			return (List<T>) listAllObj(t, map, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
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
	public static boolean updateList(List<? extends BaseRelationalDatabaseDomain> list){
		Session session = null;
		try {
			session = getSession();
			beginTransaction();
			for (BaseRelationalDatabaseDomain obj:list ) {
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
	public static int updateListNotNullFieldOneByOne(List<? extends BaseRelationalDatabaseDomain> list,List<String> updateEvenNull){
		int failed = 0;
		for (Object o:list) {
			if (!updateNotNullField((BaseRelationalDatabaseDomain) o, updateEvenNull)) {
				failed++;
			}
		}
		return failed;
	}
	
	public static void evict(Object obj){
		getSession().evict(obj);
		closeSession();
	}
	/**
	 * 如果数据库有obj对象就update否则save
	 * @param obj
	 * @return
	 */
	public static boolean saveOrUpdate(BaseRelationalDatabaseDomain obj){
		Session session = null;
		try {
			session = getSession();
			beginTransaction();
			if (BaseUtil.isObjEmpty(DomainUtil.getDomainId(obj))) {
				return save(obj);
			}else {
				BaseRelationalDatabaseDomain smartGet = smartGet(obj);
				if (smartGet != null) {
					session.evict(smartGet);
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
	
	public static int executeUpdateBySql(String hql,Map<String,? extends Object> map){
		return executeUpdate(hql, map, 1);
	}
	
	public static int executeUpdate(String hql,Map<String,? extends Object> map){
		return executeUpdate(hql, map, 0);
	}
	
	public static Object getUniqueResult(String hql,String domainSimpleName,
			Map<String,Object> map){
		try {
			StringBuffer sb = new StringBuffer(hql);
			if(domainSimpleName != null && map != null){
				appendHqlWhere(domainSimpleName, sb, map);
			}
			Query createQuery = getSession().createQuery(sb.toString());
			if (map != null) {
				setMapParam(map, createQuery);
			}
			return createQuery.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			setException(e);
			return null;
		}finally{
			closeSession();
		}
	}
	/**
	 * 
	 * @param hql
	 * @param domainSimpleName
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List getQueryResult(String hql,String domainSimpleName,
			Map<String,Object> map){
		try {
			StringBuffer sb = new StringBuffer(hql);
			if (domainSimpleName != null&&map!=null) {
				appendHqlWhere(domainSimpleName, sb, map);
			}
			Query createQuery = getSession().createQuery(sb.toString());
			if (map != null) {
				setMapParam(map, createQuery);
			}
			
			return createQuery.list();
			//return createQuery.list();
		} catch (Exception e) {
			setException(e);
			e.printStackTrace();
			return null;
		}finally{
			closeSession();
		}
	}
	@SuppressWarnings("rawtypes")
	public static Page getQueryResult(String hql,String domainSimpleName,
			Map<String,Object> map,int pageSize,int currentPage){
		try {
			StringBuffer sb = new StringBuffer(hql);
			if (map != null && domainSimpleName != null) {
				appendHqlWhere(domainSimpleName, sb, map);
			}
			
			Long recordCount = getRecordCount(map, sb);
			
			Query createQuery = getSession().createQuery(sb.toString());
			if (map != null) {
				setMapParam(map, createQuery);
			}
			faging(pageSize, currentPage, createQuery);
			
			return setPage(currentPage, recordCount, pageSize, createQuery.list());
			//return createQuery.list();
		} catch (Exception e) {
			e.printStackTrace();
			setException(e);
			return null;
		}finally{
			closeSession();
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static List getQueryResultBysql(String sql,Map<String,? extends Object> param){
		try {
			Query createQuery = getSession().createSQLQuery(sql);
			if (param != null) {
				setMapParam(param, createQuery);
			}
			return createQuery.list();
		} catch (Exception e) {
			e.printStackTrace();
			setException(e);
			return null;
		}finally{
			closeSession();
		}
	}
	@SuppressWarnings("rawtypes")
	public static List getQueryResult(String hql,Map<String,? extends Object> map,int pageSize,int currentPage){
		try {
			Query createQuery = getSession().createQuery(hql);
			if (map != null) {
				setMapParam(map, createQuery);
			}
			faging(pageSize, currentPage, createQuery);
			return createQuery.list();
		} catch (Exception e) {
			e.printStackTrace();
			setException(e);
			return null;
		}finally{
			closeSession();
		}
	}
	@SuppressWarnings("rawtypes")
	public static List getQueryResult(String hql,Map<String,? extends Object> map){
		try {
			Query createQuery = getSession().createQuery(hql);
			if (map != null) {
				setMapParam(map, createQuery);
			}
			return createQuery.list();
		} catch (Exception e) {
			setException(e);
			e.printStackTrace();
			return null;
		}finally{
			closeSession();
		}
	}
	
	private static int executeUpdate(String ql,Map<String,? extends Object> map,int type){
		Session session = null;
		try {
			session = getSession();
			beginTransaction();
			Query createQuery;
			if (type == 0) {
				createQuery = session.createQuery(ql);
			}else {
				createQuery = session.createSQLQuery(ql);
			}
			if (map != null) {
				setMapParam(map, createQuery);
			}
			int executeUpdate = createQuery.executeUpdate();
			managTransaction(true);
			return executeUpdate;
		} catch (Exception e) {
			e.printStackTrace();
			setException(e);
			managTransaction(false);
			return -1;
		}finally{
			closeSession();
		}
	}
	
	/**
	 * 通过hql更新数据库，用于批量更新
	 * @param queryCondition 查询条件，同listAllObj的查询条件
	 * @param updated 存放更新的 字段---值
	 * @return 更新的记录数，失败返回-1
	 */
	public static int updateByQuery(Object queryCondition,Map<String, Object> updated){
		return updateByQuery(queryCondition.getClass(), putField2Map(queryCondition), updated);
	}
	
	/**
	 * 通过hql更新数据库，用于批量更新
	 * @param queryCondition 查询条件，同listAllObj的查询条件
	 * @param updated 存放更新的字段-值
	 * @return 更新的记录数，失败返回-1
	 */
	public static int updateByQuery(Class<?> clazz,Map<String, Object> queryCondition,Map<String, Object> updated){
		try {
			beginTransaction();
			
			StringBuffer updateHql = getUpdateHql(clazz, queryCondition,updated);
			for (Entry<String, Object> temp:updated.entrySet()) {
				queryCondition.put("steedUpdate_"+temp.getKey(), temp.getValue());
			}
			Query query = createQuery(queryCondition, updateHql);
			int count = query.executeUpdate();
			
			if(managTransaction(true)){
				return count;
			}else {
				return -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			setException(e);
			managTransaction(false);
			return -1;
		}finally{
			closeSession();
		}
	}
	/**
	 * 以obj为查询条件删除数据库记录
	 * @param obj 查询条件
	 * @return 删除的记录数（失败返回-1）
	 */
	public static int deleteByQuery(Object obj){
		try {
			beginTransaction();
			Map<String, Object> map = new HashMap<String, Object>();
			putField2Map(obj, map, "");
			
			Query query = createQuery(map, getDeleteHql(obj.getClass(), map));
			int count = query.executeUpdate();
			
			if(managTransaction(true)){
				return count;
			}else {
				return -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			managTransaction(false);
			setException(e);
			return -1;
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
	public static boolean cascadeDelete(BaseRelationalDatabaseDomain obj,List<Class<?>> domainSkip){
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
	public static boolean delete(BaseRelationalDatabaseDomain obj){
		Session session = null;
		try {
			session = getSession();
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
	
	private static boolean deleteConneced(BaseRelationalDatabaseDomain obj,int level,List<Class<?>> domainSkip){
		if (level-- == 0) {
			return true;
		}
		Session session = null;
		try {
			session = getSession();
			BaseRelationalDatabaseDomain smartGet = DaoUtil.smartGet(obj);
			if (smartGet == null) {
				return true;
			}
			for (Field temp : smartGet.getClass().getDeclaredFields()) {
				try {
					Class<?> type = temp.getType();
					if (!domainSkip.contains(temp)) {
						if (BaseRelationalDatabaseDomain.class.isAssignableFrom(type)) {
							//TODO 支持javax.persistence.OneToOne外的其他OneToOne注解
							if (ReflectUtil.getAnnotation(OneToOne.class, smartGet.getClass(), temp) != null) {
								temp.setAccessible(true);
								BaseRelationalDatabaseDomain object = (BaseRelationalDatabaseDomain) temp.get(smartGet);
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
								// 获取Collection泛型，看是不是BaseRelationalDatabaseDomain，然后循环删除
								try {
									Class<?> c = ReflectUtil.getGenericType(temp);
									if (BaseRelationalDatabaseDomain.class.isAssignableFrom(c)) {
										for(Object o:collection){
											if(!deleteConneced((BaseRelationalDatabaseDomain)o, level, domainSkip)){
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
	
	
	/**
	 * @param t
	 * @param currentPage
	 * @param pageSize
	 * @param desc 需要降序排列的字段 可以为null
	 * @param asc 需要升序排列的字段 可以为null
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static <T> Page<T> listObj(Class<T> t,int pageSize,int currentPage,List<String> desc,List<String> asc){
		try {
			StringBuffer hql = getSelectHql(t, null, desc, asc);
			
			Long recordCount = getRecordCount(null, hql);
			
			Query query = createQuery(null, hql);
			faging(pageSize,currentPage, query);
			List list = query.list();
			
			Page<T> page = setPage(currentPage, recordCount, pageSize, list);
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			closeSession();
		}
	}
	
	public static <T extends BaseRelationalDatabaseDomain> List<T> listAllObj(Class<T> t,List<String> desc,List<String> asc){
		return listAllObj(t, null, desc, asc);
	}
	
	public static <T extends BaseRelationalDatabaseDomain> List<T> listAllObj(Class<T> t){
		/*try {
			Query query = createQuery(null, getSelectHql(t, null, null, null));
			List list = query.list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			closeSession();
		}*/
		return listAllObj(t, null, null, null);
	}
	
	public static <T> T listOne(T t){
		return listOne(t,null,null);
	}
	
	public static <T> T listOne(T t,List<String> desc,List<String> asc){
		try {
			List<T> list = (List<T>) listObj(1, 1, t,desc,asc).getDomainCollection();
			if (list.isEmpty()) {
				return null;
			}
			return (T) list.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			closeSession();
		}
	}
	/**
	 * 比如说，文章里有个用户实体类，
	 * 但是前台传过来的只有用户的id，我想获取用户的其他信息就要查数据库，
	 * 调用该方法会把baseDomain关联的所有BaseRelationalDatabaseDomain查出来
	 * @param baseDomain
	 */
	public static void getRefrenceById(BaseDomain baseDomain){
		for (Field f:baseDomain.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			Object temp;
			try {
				temp = f.get(baseDomain);
				if (temp != null & temp instanceof BaseRelationalDatabaseDomain) {
					f.set(baseDomain, smartGet((BaseRelationalDatabaseDomain)temp));
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}
	
	
	
	/**
	 * 获取所有查询对象
	 * @param <T>
	 * @param t 查询对象参数
	 * @return
	 */
	public static <T> List<T> listAllObj(T t,List<String> desc,List<String> asc){
		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>) t.getClass();
		Map<String, Object> map = new HashMap<String, Object>();
		putField2Map(t, map, "");
		return listAllObj(clazz, map,desc,asc);
	}
	/**
	 * 获取所有查询对象
	 * @param <T>
	 * @param t 查询对象参数
	 * @return
	 */
	public static <T> List<T> listAllObj(T t){
		return listAllObj(t,null,null);
	}
	
	/**
	 * 用obj做查询条件查询的结果集是否为空
	 * @param obj 查询条件
	 * @return
	 */
	public static boolean isResultNull(BaseRelationalDatabaseDomain obj){
		return getCount(obj) == 0;
	}
	
	/**
	 * 获取记录数
	 * @param query
	 * @return
	 */
	public static long getCount(BaseRelationalDatabaseDomain query){
		Class<? extends BaseRelationalDatabaseDomain> t = query.getClass();
		Map<String, Object> map = new HashMap<String, Object>();
		putField2Map(query, map, "");
		return getCount(t, map);
	}
	/**
	 * 获取所有查询对象
	 * @param t
	 * @param map 查询参数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> listAllObj(Class<T> t,Map<String, Object> map,List<String> desc,List<String> asc){
		try {
			Query query = getSession().createQuery(getSelectHql(t, map, desc, asc).toString());
			setMapParam(map, query);
			return query.list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			closeSession();
		}
	}
	/**
	 * 获取该查询条件能查到的记录数
	 * @param t
	 * @param map 查询数据
	 * @return
	 */
	public static long getCount(Class<? extends BaseRelationalDatabaseDomain> t,Map<String, Object> map){
		try {
			Query query = getSession().createQuery(getCountHql(getSelectHql(t, map, null, null)).toString());
			setMapParam(map, query);
			return (Long) query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}finally{
			closeSession();
		}
	}
	@SuppressWarnings("unchecked")
	public static <T> Page<T> listObj(int pageSize,int currentPage,T obj,List<String> desc,List<String> asc){
		Class<T> t = (Class<T>) obj.getClass();
		Map<String, Object> map = new HashMap<String, Object>();
		putField2Map(obj, map, "");
		return listObj(t,pageSize,currentPage,map,desc,asc) ;
	}
	/**
	 * 随机取size条记录
	 * @param size
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> randomlistObj(int size,T obj){
		Class<T> t = (Class<T>) obj.getClass();
		Map<String, Object> map = new HashMap<String, Object>();
		putField2Map(obj, map, "");
		return randomlistObj(t, size, map);
	}
	
	/**
	 * 随机取size条记录
	 * @param t
	 * @param size 记录数
	 * @param map 查询条件
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> randomlistObj(Class<T> t,int size,Map<String, Object> map){
		try {
			List<String> randList = new ArrayList<String>(1);
			randList.add("RAND()");
			StringBuffer hql = getSelectHql(t, map, null, null);
			hql.append(" order by RAND()");
			Query query = createQuery(map,hql);
			faging(size,1, query);
			return query.list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			closeSession();
		}
	}
	@SuppressWarnings("rawtypes")
	public static <T> Page<T> listObj(Class<T> t,int pageSize,int currentPage,Map<String, Object> map,List<String> desc,List<String> asc){
		try {
			StringBuffer hql = getSelectHql(t, map, desc, asc);
			Long recordCount = getRecordCount(map, hql);
			
			Query query = createQuery(map,hql);
			
			faging(pageSize,currentPage, query);
			List list = query.list();
			
			return setPage(currentPage, recordCount, pageSize, list);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			closeSession();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends BaseRelationalDatabaseDomain> T smartLoad(T domain){
		return (T) load(domain.getClass(), DomainUtil.getDomainId(domain));
	}
	/**
	 * 聪明的get方法
	 */
	@SuppressWarnings("unchecked")
	public static <T extends BaseRelationalDatabaseDomain> T smartGet(T domain){
		return (T) get(domain.getClass(), DomainUtil.getDomainId(domain));
	}
	public static <T extends BaseRelationalDatabaseDomain> T get(Class<T> t,Serializable key){
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
	
	public static <T extends BaseRelationalDatabaseDomain> List<T> getList(Class<T> t,Serializable[] keys){
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
	
	
	
	public static <T extends BaseRelationalDatabaseDomain> T load(Class<T> t,Serializable key){
		try {
			Session session = getSession();
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
	
	public static boolean save(BaseRelationalDatabaseDomain obj){
		try {
			Session session = getSession();
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
	public static boolean updateNotNullField(BaseRelationalDatabaseDomain obj,List<String> updateEvenNull){
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
	public static boolean updateNotNullField(BaseRelationalDatabaseDomain obj,List<String> updateEvenNull,boolean strictlyMode){
		List<String> list;
		if (updateEvenNull == null) {
			list = new ArrayList<String>();
			for (Field f:ReflectUtil.getAllFields(obj)) {
				if (f.getAnnotation(UpdateEvenNull.class) != null) {
					list.add(f.getName());
				}
			}
		}else {
			list = updateEvenNull;
		}
		return update(DomainUtil.fillDomain(smartGet(obj), obj,list,strictlyMode));
	}
	
	public static boolean update(BaseRelationalDatabaseDomain obj){
		try {
			Session session = getSession();
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
	public static int updateListOneByOne(List<? extends BaseRelationalDatabaseDomain> list){
		int failed = 0;
		for (BaseRelationalDatabaseDomain o:list) {
			if (!update(o)) {
				failed++;
			}
		}
		return failed;
	}
	/***************************增删查改结束******************************/
	
	
	
	
	/***************************数据库工具开始******************************/
	
	/**
	 * 获取更新用的hql
	 * 除了t其它均可为null
	 * @param t 实体类
	 * @param map 查询数据
	 * @return 拼好的hql
	 */
	public static <T> StringBuffer getUpdateHql(Class<T> t,
			Map<String, Object> queryCondition,Map<String, Object> updated) {
		String fullClassName = t.getName();
		StringBuffer hql = new StringBuffer();
		String domainSimpleName = getDomainSimpleName(fullClassName);
		
		hql.append(" update ")
			.append(fullClassName)
			.append(" ")
			.append(domainSimpleName);
		hql.append(" set ");
		appendHqlUpdateSet(hql, domainSimpleName, updated);
		
		hql.append(" where 1=1 ");
		
		appendHqlWhere(domainSimpleName, hql, queryCondition);
		
		logger.debug("hql------>"+hql.toString());
		return hql;
	}
	/**
	 * 获取删除用的hql
	 * 除了t其它均可为null
	 * @param t 实体类
	 * @param map 查询数据
	 * @return 拼好的hql
	 */
	public static <T> StringBuffer getDeleteHql(Class<T> t,Map<String, Object> map) {
		return getHql(t, map, null, null,"delete");
	}
	/**
	 * 获取查询用的hql
	 * 除了t其它均可为null
	 * @param t 实体类
	 * @param map 查询数据
	 * @param desc 降序排列字段
	 * @param asc 升序排列字段
	 * @return 拼好的hql
	 */
	public static <T> StringBuffer getSelectHql(Class<T> t,Map<String, Object> map,List<String> desc,List<String> asc) {
		return getHql(t, map, desc, asc,"select");
	}
	/**
	 * 
	 * 获取hql
	 * 除了t其它均可为null
	 * @param t 实体类
	 * @param queryMap 查询数据
	 * @param desc 降序排列字段
	 * @param asc 升序排列字段
	 * @param prefix hql前面部分目前只支持"select"或"delete"
	 * @return 拼好的hql
	 */
	public static <T> StringBuffer getHql(Class<T> t, Map<String, Object> queryMap,List<String> desc, List<String> asc,String prefix) {
		String fullClassName = t.getName();
		StringBuffer hql = new StringBuffer();
		String domainSimpleName = getDomainSimpleName(fullClassName);
		
		hql.append(prefix);
		
		if ("select".equals(prefix)) {
			hql.append(" ").append(domainSimpleName);
		}
		
		hql.append(" from ")
			.append(fullClassName)
			.append(" ")
			.append(domainSimpleName)
			.append(" where 1=1 ");
		
		appendHqlWhere(domainSimpleName, hql, queryMap);
		appendHqlOrder(hql, desc, asc, domainSimpleName);
		
		logger.debug("hql------>"+hql.toString());
		
		return hql;
	}
	/**
	 * 根据查询对象生成hql
	 * @param domain
	 * @param desc 需要降序排列的字段
	 * @param asc 需要升序排列的字段
	 * @return 生成的hql
	 */
	public static StringBuffer getSelectHql(BaseRelationalDatabaseDomain domain,List<String> desc,List<String> asc) {
		Class<?> clazz = domain.getClass();
		Map<String, Object> map = new HashMap<String, Object>();
		putField2Map(domain, map, "");
		return getSelectHql(clazz, map, desc, asc);
	}
	
	/**
	 * 获取该条查询的记录总数
	 * @param hql
	 * @return
	 */
	public static StringBuffer getCountHql(StringBuffer hql) {
		return changeSelectHql(hql, "count(*)");
	}
	/**
	 * 获取该条查询的记录总数
	 * @param hql
	 * @return
	 */
	public static StringBuffer changeSelectHql(StringBuffer hql,String selectedField) {
		//select people_steed_00 inner join people_steed_00.roleSet people_steed_00roleSet from steed.domain.people.People people_steed_00 where 1=1 
		//and people_steed_00roleSet in (:roleSet) and people_steed_00roleSet not in (:roleSet_not_in_1)
		StringBuffer countHql = new StringBuffer(hql);
		Pattern p = Pattern.compile("select .+? from");
		Matcher m = p.matcher(countHql);
		if (m.find()) {
			countHql.replace(m.start(0), m.end(0), "select "+selectedField+" from");
		}else {
			countHql.insert(0, "select "+selectedField+" ");
		}
		logger.debug("countHql--->"+countHql);
		return countHql;
	}
	
	/**
	 * 生成query并设置查询参数
	 * @param map
	 * @param hql
	 * @return
	 */
	public static Query createQuery(Map<String, Object> map,StringBuffer hql) {
		logger.debug("hql---->"+hql.toString());
		logger.debug("参数---->"+map);
		Query query = getSession().createQuery(hql.toString());
		setMapParam(map, query);
		return query;
	}
	
	protected static Session getSession(){
		if (currentTransaction.get() == null) {
			currentTransaction.set(HibernateUtil.getSession().beginTransaction());
		}
		return HibernateUtil.getSession();
	}
	/**
	 * 根据配置关闭session
	 */
	public static void closeSession(){
		managTransaction(true);
		if (HibernateUtil.getColseSession() && (autoManagTransaction.get() == null || autoManagTransaction.get())) {
			managTransaction();
			HibernateUtil.closeSession();
		}
	}
	/***************************数据库工具结束********************************/
	
	
	
	/***************************内部方法开始********************************/
	
	/**
	 * 获取domain的简称用作查询时的别名
	 * @param fullClassName 全类名
	 * @return
	 */
	private static String getDomainSimpleName(String fullClassName) {
		String domainSimpleName = StringUtil.firstChar2LowerCase(StringUtil.getClassSimpleName(fullClassName)+"_steed_00");
		return domainSimpleName;
	}
	/**
	 * 组装hql的order by 部分
	 * @param hql
	 * @param desc
	 * @param asc
	 * @param domainSimpleName
	 * @return
	 */
	private static StringBuffer appendHqlOrder(StringBuffer hql,List<String> desc,List<String> asc,String domainSimpleName){
		boolean hasOrderByAppened = false;
		if (desc != null && !desc.isEmpty()) {
			for (String name:desc) {
				if (!hasOrderByAppened) {
					hql.append("order by ");
					hasOrderByAppened = true;
				}else {
					hql.append(", ");
				}
				hql.append(domainSimpleName);
				hql.append(".");
				hql.append(name);
				hql.append(" desc");
			}
		}
		if (asc != null && !asc.isEmpty()) {
			for (String name:asc) {
				if (!hasOrderByAppened) {
					hql.append("order by ");
					hasOrderByAppened = true;
				}else {
					hql.append(", ");
				}
				hql.append(domainSimpleName);
				hql.append(".");
				hql.append(name);
				hql.append(" asc");
			}
		}
		return hql;
	}
	@SuppressWarnings("unused")
	private static StringBuffer appendHqlGroupBy(StringBuffer hql,List<String> groupBy,String domainSimpleName){
		boolean hasOrderByAppened = false;
		if (!CollectionsUtil.isCollectionsEmpty(groupBy)) {
			for (String temp:groupBy) {
				if (!hasOrderByAppened) {
					hql.append("group by ");
					hasOrderByAppened = true;
				}else {
					hql.append(", and ");
				}
				if (domainSimpleName != null) {
					hql.append(domainSimpleName)
						.append(".");
				}
				hql.append(temp)
					.append(" ");
			}
		}
		return hql;
	}
	/**
	 *  把obj中非空字段放到map
	 * @param obj
	 * @param map
	 */
	public static Map<String, Object> putField2Map(Object obj) {
		Map<String, Object> map = new HashMap<>();
		putField2Map(obj, map, "");
		return map;
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
				if (!isSelectIndex(fieldName)) {
/*					if (f.getAnnotation(Transient.class) != null) {
						continue;
					}
*/					if (ReflectUtil.getAnnotation(NotQueryCondition.class, objClass, f) != null) {
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
				}
				
				f.setAccessible(true);
				Object value = f.get(obj);
				if (!BaseUtil.isObjEmpty(value)) {
					if (value instanceof BaseRelationalDatabaseDomain ) {
						JoinColumn annotation = ReflectUtil.getAnnotation(JoinColumn.class, objClass, f);
						if (annotation == null 
								|| !(!annotation.insertable() 
										&& !annotation.updatable())) {
							map.put(prefixName + fieldName, value);
						}
					}else {
						map.put(prefixName + fieldName, value);
					}
				}else if (value instanceof BaseRelationalDatabaseDomain 
						&& !(value instanceof BaseUnionKeyDomain)
						&& BaseUtil.isObjEmpty(DomainUtil.getDomainId((BaseDomain) value))) {
					//实体类级联查询支持,离0hql的伟大构想已经非常接近了!
					putField2Map(value, map,fieldName +".");
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 是否属于查找索引字段
	 * @param fieldName
	 * @return
	 */
	public static boolean isSelectIndex(String fieldName){
		for (String suffix:indexSuffix) {
			if (fieldName.endsWith(suffix)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 组装map参数到hql的where部分
	 * @param domainSimpleName
	 * @param hql
	 * @param map
	 */
	public static StringBuffer appendHqlWhere(String domainSimpleName, StringBuffer hql,
			Map<String, ? extends Object> map) {
		return hqlGenerator.appendHqlWhere(domainSimpleName, hql, map);
	}
	
	/**
	 * 组装update类型的hql中set部分
	 */
	private static void appendHqlUpdateSet(StringBuffer hql,String domainSimpleName,Map<String, Object> updated){
		for (String temp:updated.keySet()) {
			hql.append(domainSimpleName)
				.append(".")
				.append(temp)
				.append("=:steedUpdate_")
				.append(temp)
				.append(", ");
		}
		hql.deleteCharAt(hql.length()-2);
	}
	
	/**
	 * 获取总记录
	 * @param map 可以为null
	 * @param domainName
	 * @param hql 普通查询hql,会自动转换成查询记录总数的hql
	 * @return
	 */
	private static Long getRecordCount(Map<String, Object> map,StringBuffer hql) {
		Query query = createQuery(map, getCountHql(hql));
		Long recordCount = (Long) query.uniqueResult();
		return recordCount;
	}

	/**
	 * 设置page的大小，当前页等
	 * @param currentPage
	 * @param recordCount
	 * @param pageSize
	 * @param list
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static <T> Page<T> setPage(int currentPage, Long recordCount,
			int pageSize, List list) {
		Page<T> page = new Page<T>();
		page.setCurrentPage(currentPage);
		page.setPageSize(pageSize);
		page.setRecordCount(recordCount);
		page.setDomainCollection(list);
		return page;
	}
	/**
	 * 把map中的查询数据设置到query
	 * @param map
	 * @param query
	 */
	private static void setMapParam(Map<String, ? extends Object> map, Query query) {
		if (CollectionsUtil.isCollectionsEmpty(map)) {
			return;
		}
		String key;
		for (Entry<String, ? extends Object> e:map.entrySet()) {
			key = e.getKey();
			Object value = e.getValue();
			if (value == null) {
				query.setParameter(key.replaceAll("\\.", "__"), value);
			}else {
				if (value instanceof Collection) {
					query.setParameterList(key.replaceAll("\\.", "__"), (Collection<?>) value);
				}else if(value.getClass().isArray()){
					query.setParameterList(key.replaceAll("\\.", "__"), (Object[]) value);
				}else {
					query.setParameter(key.replaceAll("\\.", "__"), value);
				}
			}
		}
	}
	/***************************内部方法结束********************************/

	/**
	 * 设置query分页
	 * @param currentPage
	 * @param query
	 * @return
	 */
	public static void faging(int pageSize,int currentPage, Query query) {
//		int pageSize = PropertyUtil.getConfig("page.size", Integer.class);
		query.setFirstResult((currentPage-1)*pageSize);
		query.setMaxResults(pageSize);
	}
	
	public static class ImmediatelyTransactionData{
		Transaction currentTransaction;
		Boolean autoManagTransaction;
		public ImmediatelyTransactionData(Transaction currentTransaction,Boolean autoManagTransaction) {
			this.currentTransaction = currentTransaction;
			this.autoManagTransaction = autoManagTransaction;
		}
		
	}
}
