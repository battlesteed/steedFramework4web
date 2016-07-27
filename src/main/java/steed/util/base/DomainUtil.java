package steed.util.base;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.IdClass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import steed.domain.BaseDomain;
import steed.domain.BaseUser;
import steed.domain.UnionKeyDomain;
import steed.exception.runtime.domain.DomainIdAnnotationNotFoundException;
import steed.exception.runtime.system.FrameworkException;
import steed.util.dao.DaoUtil;
import steed.util.reflect.ReflectUtil;
/**
 * 实体类工具类
 * @author 战马
 *
 */
public class DomainUtil{
	private static Logger logger = LoggerFactory.getLogger(DomainUtil.class);
	/**
	 * 获取实体类的hashcode
	 * @param baseDomain
	 * @return
	 */
	public static int domainHashCode(BaseDomain baseDomain){
		if (baseDomain == null) {
			return 0;
		}
		try {
			Object tempObject = getDomainId(baseDomain);
			if (tempObject == null) {
				return -1;
			}else {
				return tempObject.hashCode();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
	}
	/**
	 * 获取实体类ID的名字
	 * @param clazz
	 * @return
	 */
	public static String getDomainIDName(Class<? extends BaseDomain> clazz){
		/**
		 * 如果是联合主键实体类则返回domainID，因为联合主键类必须实现getDomainID()方法
		 */
		if (clazz.getAnnotation(IdClass.class) != null) {
			return "domainID";
		}
		try {
			Method m = getIDmethod(clazz);
			if (m != null) {
				String name = m.getName();
				if (name.startsWith("is")) {
					return name.substring(2, 3).toLowerCase() + name.substring(3) ;
				}else {
					return name.substring(3, 4).toLowerCase() + name.substring(4) ;
				}
			}
		} catch (Exception e) {
			throw createFindIdException(clazz, e);
		}
		
		try {
			Field f = getIDfield(clazz);
			if (f != null) {
				return f.getName();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw createFindIdException(clazz, e);
		}  
		throw createIDNotfoundException(clazz);
	}
	/**
	 * 获取实体类ID Class
	 * @param clazz
	 * @return
	 */
	public static Class<? extends Serializable> getDomainIDClass(Class<? extends BaseDomain> clazz){
		/**
		 * 如果是联合主键实体类则返回domainID，因为联合主键类必须实现getDomainID()方法
		 */
		IdClass annotation = clazz.getAnnotation(IdClass.class);
		if (annotation != null) {
			return annotation.value();
		}
		try {
			Field f = clazz.getDeclaredField(getDomainIDName(clazz));
			if (f != null) {
				return (Class<? extends Serializable>) f.getType();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw createFindIdException(clazz, e);
		}  
		throw createIDNotfoundException(clazz);
	}
	
	public static BaseDomain fillID2Domain(Serializable id,BaseDomain baseDomain){
		Class<? extends BaseDomain> domainClass = baseDomain.getClass();
		Field iDfield = getIDfield(domainClass);
		try {
			if (iDfield != null) {
				iDfield.setAccessible(true);
				iDfield.set(baseDomain, id);
				return baseDomain;
			}else {
				Method iDmethod = getIDmethod(domainClass);
				if (iDmethod != null) {
						domainClass.getMethod(iDmethod.getName().replaceFirst("get", "set"),iDmethod.getReturnType()).invoke(baseDomain, id);
						return baseDomain;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(domainClass.getName()+"中没有id字段或ID的set方法！！");
		}
		throw new RuntimeException(domainClass.getName()+"中没有id字段或ID的set方法！！");
	}
	
	
	
	/**
	 * 获取实体类所属用户
	 * @param baseDomain
	 * @return
	 */
	public static BaseUser getDomainUser(BaseDomain baseDomain){
		Class<? extends BaseDomain> clazz = baseDomain.getClass();
		for (Field f:clazz.getDeclaredFields()) {
			f.setAccessible(true);
			Object object;
			try {
				object = f.get(baseDomain);
				if (object instanceof BaseUser) {
					return (BaseUser) object;
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * 获取实体类主键
	 * @param baseDomain
	 * @return
	 */
	public static Serializable getDomainId(BaseDomain baseDomain){
		if (baseDomain instanceof UnionKeyDomain) {
			return ((UnionKeyDomain)baseDomain).getDomainID();
		}
		Class<? extends BaseDomain> clazz = baseDomain.getClass();
		try {
			Method m = getIDmethod(clazz);
			if (m != null) {
   				return (Serializable) m.invoke(baseDomain);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw createFindIdException(clazz, e);
		}
		
		try {
			Field f = getIDfield(clazz);
			if (f != null) {
				return (Serializable) f.get(baseDomain);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw createFindIdException(clazz, e);
		}  
		throw createIDNotfoundException(clazz);
	}

	private static RuntimeException createFindIdException(Class<? extends BaseDomain> clazz,
			Exception e) {
		logger.error("在"+
				clazz.getName()+"找到含有"+Id.class.getName()+"注解的get方法,或字段时出现异常。", e);
		return new RuntimeException("在"+
				clazz.getName()+"找到含有"+Id.class.getName()+"注解的get方法,或字段时出现异常。", e);
	}

	private static DomainIdAnnotationNotFoundException createIDNotfoundException(Class<? extends Object> clazz) {
		logger.error("在"+clazz.getName()+"中找不到含有"+Id.class.getName()+"注解的get方法,或字段。");
		DomainIdAnnotationNotFoundException exception = new DomainIdAnnotationNotFoundException("在"+
				clazz.getName()+"中找不到含有"+Id.class.getName()+"注解的get方法,或字段。");
		exception.printStackTrace();
		return exception;
	}
	
	/**
	 * 判断实体类和另一个对象是否相等
	 * @param baseDomain
	 * @param obj2
	 * @return
	 */
	public static boolean domainEquals(BaseDomain baseDomain,Object obj2){
		if (baseDomain == null) {
			return obj2 == null;
		}else if (obj2 == null) {
			return false;
		}
//		Class<? extends BaseDomain> clazz = baseDomain.getClass();
		Class<? extends Object> clazz2 = obj2.getClass();
		if (BaseDomain.class.isAssignableFrom(clazz2)) {
			/*Method iDmethod = getIDmethod(clazz);
			if (iDmethod != null) {
				Object tempObject;
				try {
					tempObject = iDmethod.invoke(baseDomain);
					return BaseUtil.objectEquals(iDmethod.invoke(obj2),tempObject);
				} catch (Exception e) {
					e.printStackTrace();
					throw createFindIdException(clazz, e);
				} 
			}
			Field f = getIDfield(clazz);
			if (f != null) {
				try {
					Object tempObject = f.get(baseDomain);
					return BaseUtil.objectEquals(f.get(obj2),tempObject);
				} catch (Exception e) {
					e.printStackTrace();
					throw createFindIdException(clazz, e);
				} 
			}
			createIDNotfoundException(clazz2);*/
			
			Serializable domainId = getDomainId(baseDomain);
			Serializable id2 = getDomainId((BaseDomain) obj2);
			
			if (domainId == null) {
				return id2 == null;
			}else if(id2 == null){
				return false;
			}else {
				return id2.equals(domainId);
			}
			
		}else if(ReflectUtil.isObjBaseData(obj2)){
			Serializable domainId = getDomainId(baseDomain);
			return obj2.equals(domainId);
		}else {
			return false;
		}
		
	}
	
	public static void copyDomainSameField(Object copy,Object copyed){
		Class clazz = copyed.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field f:fields) {
			try {
				if (!"serialVersionUID".equals(f.getName())) {
					f.setAccessible(true);
					Object value = f.get(copyed);
					if (value == null) {
						continue;
					}
					try {
						Field declaredField = copy.getClass().getDeclaredField(f.getName());
						declaredField.setAccessible(true);
						declaredField.set(copy, value);
					} catch (NoSuchFieldException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					}
				}
			} catch (IllegalArgumentException e) {
				logger.debug("copyDomainSameField", e);
			} catch (IllegalAccessException e) {
				logger.debug("copyDomainSameField", e);
			}
		}
	}
	
	public static <T extends BaseDomain> T copyDomain(T copy){
		Class<? extends BaseDomain> clazz = copy.getClass();
		Field[] fields = clazz.getDeclaredFields();
		T domain;
		try {
			domain = (T) clazz.newInstance();
			for (Field f:fields) {
				f.setAccessible(true);
				try {
					Object value = f.get(copy);
					f.set(domain, value);
				} catch (IllegalArgumentException e) {
					logger.debug("copyDomain", e);
				} catch (IllegalAccessException e) {
					logger.debug("copyDomain", e);
				}
			}
			return domain;
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 获取ID字段
	 * @param clazz
	 * @return
	 */
	private static Field getIDfield(Class<? extends BaseDomain> clazz) {
		if (clazz.getAnnotation(IdClass.class) != null) {
			StringBuffer sb = new StringBuffer("按照约定含有");
			sb.append(IdClass.class.getName());
			sb.append("注解的domain不能有ID字段!!!只能有getDomainID()方法。");
			ExceptionUtil.throwRuntimeException(sb, logger);
		}
		Field[] fields = clazz.getDeclaredFields();
		for (Field f:fields) {
			f.setAccessible(true);
			Annotation temp = f.getAnnotation(Id.class);
			if (temp == null) {
				continue;
			}
			return f;
		}
		if (clazz == BaseDomain.class) {
			return null;
		}
		return getIDfield((Class<? extends BaseDomain>)clazz.getSuperclass());
	}
	
	private static Method getIDmethod(Class<? extends BaseDomain> clazz) {
		if (clazz.getAnnotation(IdClass.class) != null) {
			try {
				return clazz.getDeclaredMethod("getDomainID");
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
				ExceptionUtil.throwRuntimeException("含有"+IdClass.class.getName()+
						"的domain必须实现"+UnionKeyDomain.class.getName()+"接口!!!", logger);
			}
		}
		
		Method[] methods = clazz.getDeclaredMethods();
		for (Method m:methods) {
			Annotation aa = m.getAnnotation(Id.class);
			if (aa == null) {
				continue;
			}
			return m;
		}
		if (clazz == BaseDomain.class) {
			return null;
		}
		return getIDmethod((Class<? extends BaseDomain>)clazz.getSuperclass());
	}
	/**
	 * 把fill中不为null的字段填充给filled
	 * @param filled
	 * @param fill
	 * @param fieldsNotSkip 即使为null也不跳过的字段,如果没有可以传个null
	 * @return 
	 * @return filled
	 */
	public static <T> T fillDomain(T filled,T fill,Collection<String> fieldsNotSkip){
		return fillDomain(filled, fill, fieldsNotSkip, false);
	}
	/**
	 * 把fill中不为null的字段填充给filled
	 * @param filled
	 * @param fill
	 * @param fieldsNotSkip 即使为null也不跳过的字段,如果没有可以传个null
	 *  @param strictlyMode 严格模式，如果为true则 字段==null才算空，
	 * 	否则调用BaseUtil.isObjEmpty判断字段是否为空
	 * @see BaseUtil#isObjEmpty
	 * @return 
	 * @return filled
	 */
	public static <T> T fillDomain(T filled,T fill,Collection<String> fieldsNotSkip,boolean strictlyMode){
		Class clazz = fill.getClass();
		Field[] fields = clazz.getDeclaredFields();
		try {
			if(fieldsNotSkip == null){
				fieldsNotSkip = new ArrayList<String>();
			}
			for (Field f:fields) {
				f.setAccessible(true);
				Object temp = f.get(fill);
				boolean isNull;
				if (strictlyMode) {
					isNull = temp == null;
				}else {
					isNull = BaseUtil.isObjEmpty(temp);
				}
				if ((!isNull && !"serialVersionUID".equals(f.getName()))
						|| fieldsNotSkip.contains(f.getName())) {
					try {
						f.set(filled, temp);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			return filled;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
			return null;
		} 
	}
	
	/**
	 * 初始化查询实体类模糊查询
	 * @param obj
	 * @param fieldsSkip 跳过的字段，不跳请传空
	 */
	public static void FuzzyQueryInitialize(Object obj,String ...fieldsSkip){
		List<String> fieldsSkipList = new ArrayList<String>();
		Collections.addAll(fieldsSkipList, fieldsSkip);
		List<Field> allFields = ReflectUtil.getAllFields(obj);
		for (Field f:allFields) {
			try {
				if (DaoUtil.isSelectIndex(f.getName())) {
					continue;
				}
				f.setAccessible(true);
				Object value = f.get(obj);
				if (value instanceof String && !StringUtil.isStringEmpty((String) value)&&!fieldsSkipList.contains(f.getName())) {
					f.set(obj, "%"+value+"%");
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static void setDomainId(BaseDomain baseDomain,
			Serializable serializable) {
		Class<? extends BaseDomain> class1 = baseDomain.getClass();
		String domainIDName = getDomainIDName(class1);
		String fieldSetterName = StringUtil.getFieldSetterName(domainIDName);
		try {
			Method method = class1.getMethod(fieldSetterName, serializable.getClass());
			method.invoke(baseDomain, serializable);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new FrameworkException(class1+"中没有"+fieldSetterName+"方法", e);
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new FrameworkException(class1+"中的"+fieldSetterName+"方法不是public类型", e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new FrameworkException(class1+"中的"+fieldSetterName+"方法不是public类型", e);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new FrameworkException(class1+"中的"+fieldSetterName+"方法不是public类型", e);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new FrameworkException(class1+"中的"+fieldSetterName+"方法不是public类型", e);
		}
	}
}
