package steed.hibernatemaster.util.base;

public class StringUtil {
	public static boolean isStringEmpty(String str){
		return null == str || "".equals(str);
	}
	
	/**
	 * 将str的第一个字母改为小写
	 * @param str
	 * @return
	 */
	public static String firstChar2LowerCase(String str){
		StringBuffer sb = new StringBuffer(String.valueOf(str.charAt(0)).toLowerCase());
		sb.append(str.substring(1, str.length()));
		return sb.toString();
	}
	
	
	/**
	 * 获取类名简写
	 * @return
	 */
	public static String getClassSimpleName(String classFullName){
		return classFullName.substring(classFullName.lastIndexOf(".")+1, classFullName.length());
	}
	/**
	 * 根据字段名获取该字段的is方法名,
	 * 如字段不是Boolean类型请使用getFieldGetMethodName
	 * @param fieldName
	 * @return
	 */
	public static String getFieldIsMethodName(String fieldName){
		StringBuffer sb = new StringBuffer("is");
		sb.append(firstChar2UpperCase(fieldName));
		return sb.toString();
	}
	
	/**
	 * 根据字段名获取该字段的get方法名,不支持boolean类型，
	 * 如字段为Boolean类型请使用getFieldISMethodName
	 * @param fieldName
	 * @return
	 */
	public static String getFieldGetterName(String fieldName){
		StringBuffer sb = new StringBuffer("get");
		sb.append(firstChar2UpperCase(fieldName));
		return sb.toString();
	}
	
	/**
	 * 根据字段名获取该字段的set方法名
	 * @param fieldName
	 * @return
	 */
	public static String getFieldSetterName(String fieldName){
		StringBuffer sb = new StringBuffer("set");
		sb.append(firstChar2UpperCase(fieldName));
		return sb.toString();
	}
	/**
	 * 将str的第一个字母改为大写
	 * @param str
	 * @return
	 */
	public static String firstChar2UpperCase(String str){
		StringBuffer sb = new StringBuffer(String.valueOf(str.charAt(0)).toUpperCase());
		sb.append(str.substring(1, str.length()));
		return sb.toString();
	}
}
