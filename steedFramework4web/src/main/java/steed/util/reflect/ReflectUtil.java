package steed.util.reflect;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import steed.exception.runtime.system.FrameworkException;
import steed.util.base.BaseUtil;
import steed.util.base.PropertyUtil;
import steed.util.base.StringUtil;

public class ReflectUtil {
	public static <T> T getInstanceFromProperties(String key,String propertiesFile){
		String className = PropertyUtil.getProperties(propertiesFile).getProperty(key);
		if (StringUtil.isStringEmpty(className)) {
			return null;
		}
		try {
			return (T) Class.forName(className).newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 把string转换成基本ID类型
	 * @param baseType
	 * @param str
	 * @return
	 */
	public static Serializable string2BaseID(Class baseType,String str){
		if (baseType == String.class) {
			return str;
		}
		if (baseType == Integer.class) {
			return Integer.parseInt(str);
		}
		if (baseType == Long.class) {
			return Long.parseLong(str);
		}
		if (baseType == Double.class) {
			return Double.parseDouble(str);
		}
		if (baseType == Float.class) {
			return Float.parseFloat(str);
		}
		if (baseType == Boolean.class) {
			return Boolean.parseBoolean(str);
		}
		if (baseType == Short.class) {
			return Short.parseShort(str);
		}
		throw new RuntimeException(baseType.getName()+"不是基本ID类型");
	}
	public static boolean isClassBaseID(Class clazz){
		return clazz == String.class || 
				clazz == Short.class ||
				clazz == Integer.class||
				clazz == Float.class ||
				clazz == Long.class ||
				clazz == Character.class ||
				clazz == Double.class;
	}
	public static boolean isClassBaseType(Class clazz){
		return clazz == Byte.class || 
				clazz == Short.class ||
				clazz == Integer.class||
				clazz == Float.class ||
				clazz == Boolean.class ||
				clazz == Character.class ||
				clazz == Double.class ||
				clazz == Long.class;
	}
	
	public static boolean isObjBaseType(Object obj){
		return obj instanceof Byte || 
				obj instanceof Short ||
				obj instanceof Integer||
				obj instanceof Float ||
				obj instanceof Boolean ||
				obj instanceof Character ||
				obj instanceof Long ||
				obj instanceof Double;
	}
	
	
	public static boolean isFieldFinal(Field field){
		return (field.getModifiers()&Modifier.FINAL)==Modifier.FINAL;
	}
	public static Map<String, Object> field2Map(Object obj){
		Map<String, Object> map = new HashMap<String, Object>();
		return field2Map(0,obj, map);
	}
	public static Map<String, Object> field2Map(int classDdeep,Object obj,Map<String, Object> map){
		Class tempClass = obj.getClass();
		for (int i = 0; i < classDdeep; i++) {
			tempClass = tempClass.getSuperclass();
		}
		if (tempClass == Object.class) {
			return map;
		}
		for (Field temp:tempClass.getDeclaredFields()) {
			temp.setAccessible(true);
			try {
				Object obj2 = temp.get(obj);
				if (!BaseUtil.isObjEmpty(obj2)) {
					map.put(temp.getName(), obj2);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		return field2Map(++classDdeep, obj, map);
	}
	
	
	public static void setValue(String fieldName,Object obj,Object value){
		Throwable throwable;
		try {
			Field declaredField = obj.getClass().getDeclaredField(fieldName);
			declaredField.setAccessible(true);
			declaredField.set(obj, value);
			return;
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			throwable = e;
		} catch (SecurityException e) {
			e.printStackTrace();
			throwable = e;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throwable = e;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throwable = e;
		}
		throw new FrameworkException(throwable);
	}
	public static Object getValue(String fieldName,Object obj){
		Throwable throwable;
		try {
			Field declaredField = obj.getClass().getDeclaredField(fieldName);
			declaredField.setAccessible(true);
			return declaredField.get(obj);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			throwable = e;
		} catch (SecurityException e) {
			e.printStackTrace();
			throwable = e;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throwable = e;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throwable = e;
		}
		throw new FrameworkException(throwable);
	}
	
	public static Object newInstance(String className){
		try {
			return Class.forName(className).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		throw new RuntimeException(className+"实例化失败！！");
	}
	
	public static <T extends Annotation> T getAnnotation(Class<T> annotationClass,
			Class<? extends Object> objClass,Field field){
		T annotation = field.getAnnotation(annotationClass);
		if (annotation != null) {
			return annotation;
		}
		
		String name = field.getName();
		String fieldGetterName = StringUtil.getFieldGetterName(name);
		try {
			Method declaredMethod = objClass.getDeclaredMethod(fieldGetterName);
			T annotation2 = declaredMethod.getAnnotation(annotationClass);
			if (annotation2 != null) {
				return annotation2;
			}
		} catch (NoSuchMethodException e) {
		} catch (SecurityException e) {
		}
		
		String fieldIsMethodName = StringUtil.getFieldIsMethodName(name);
		try {
			Method declaredMethod3 = objClass.getDeclaredMethod(fieldIsMethodName);
			T annotation3 = declaredMethod3.getAnnotation(annotationClass);
			if (annotation3 != null) {
				return annotation3;
			}
		} catch (NoSuchMethodException e) {
		} catch (SecurityException e) {
		}
		return null;
		
	}
	public static List<Annotation> getAnnotations(Class<? extends Object> objClass,Field field){
		List<Annotation> list = new ArrayList<Annotation>();
		Collections.addAll(list, field.getDeclaredAnnotations());
		String name = field.getName();
		String fieldGetterName = StringUtil.getFieldGetterName(name);
		try {
			Method declaredMethod = objClass.getDeclaredMethod(fieldGetterName);
			Collections.addAll(list, declaredMethod.getAnnotations());
		} catch (NoSuchMethodException e) {
		} catch (SecurityException e) {
		}
		
		String fieldIsMethodName = StringUtil.getFieldIsMethodName(name);
		try {
			Method declaredMethod3 = objClass.getDeclaredMethod(fieldIsMethodName);
			Collections.addAll(list, declaredMethod3.getAnnotations());
		} catch (NoSuchMethodException e) {
		} catch (SecurityException e) {
		}
		return list;
		
	}
	
	
	public static Class<?> getGenericType(Field f) {
		try {
	        Type mapMainType = f.getGenericType();
	        if (mapMainType instanceof ParameterizedType) {   
	            // 执行强制类型转换   
	            ParameterizedType parameterizedType = (ParameterizedType)mapMainType;   
	            // 获取基本类型信息，即Map   
	            Type basicType = parameterizedType.getRawType();   
	            // 获取泛型类型的泛型参数   
	            Type[] types = parameterizedType.getActualTypeArguments();   
	            /*for (int i = 0; i < types.length; i++) {   
	                System.out.println("第"+(i+1)+"个泛型类型是："+types[i]);   
	            }  */ 
	            return (Class) types[0];
	        } else {   
	          throw new RuntimeException(String.format("在%s字段找不到泛型信息！！", f.getName())); 
	        }   
		}  catch (SecurityException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<Field> getAllFields(Object object){
		List<Field> fieldList = new ArrayList<Field>();
		Class<? extends Object> class1 = object.getClass();
		while (class1 != Object.class) {
			Collections.addAll(fieldList, class1.getDeclaredFields());
			class1 = class1.getSuperclass();
		}
		return fieldList;
	}
	
	public static Method getDeclaredMethod(Class<?> clazz,String name, Class<?>... parameterTypes){
		while (clazz != Object.class) {
			try {
				return clazz.getDeclaredMethod(name, parameterTypes);
			} catch (NoSuchMethodException | SecurityException e) {
				clazz = clazz.getSuperclass();
			}
		}
		return null;
	}
	
	
	/**
	 * 判断该类型是否是数据库基本数据类型
	 * @param obj
	 * @return
	 */
	public static boolean isObjBaseData(Object obj){
		return isClassBaseData(obj.getClass());
//		return isObjBaseType(obj) || obj instanceof String || obj instanceof Date;
	}
	/**
	 * 判断该类型是否是数据库基本数据类型
	 * @param obj
	 * @return
	 */
	public static boolean isClassBaseData(Class clazz){
		return isClassBaseType(clazz) || clazz == String.class || Date.class.isAssignableFrom(clazz);
	}
}
