package steed.hibernatemaster.util.base;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

public class PathUtil {

	/**
	 * 获取classes目录在操作系统中的绝对路径 形如D:/workspaces/SMS/WebRoot/WEB-INF/classes/
	 * @return
	 */
	public static String getClassesPath(){
		return PathUtil.class.getResource("/").getPath();
	}
	
	/**
	 * 合并路径,防止出现双斜杠或者没有斜杠
	 * @return
	 */
	public static String mergePath(String path1,String path2){
		return mergePath(path1, path2, "/");
	}
	
	public static String mergePath(String path1,String path2,String separator){
		if (path2.startsWith(separator)&&path1.endsWith(separator)) {
			return path1 + path2.substring(1);
		}else if(!path2.startsWith(separator)&&!path1.endsWith(separator)){
			return path1 + separator + path2;
		}else {
			return path1 + path2;
		}
	}
	

	/**
	 * 把分隔符换成当前系统的分割符,如更换后路径以类似\D:开头,
	 * 开头的\将会被去掉
	 * @param path
	 * @return
	 */
	public static String change2SystemSeparatorChar(String path){
		char separatorChar = File.separatorChar;
		switch (separatorChar) {
		case '/':
			path =  path.replaceAll("\\\\", "/");
			break;
		case '\\':
			path = path.replaceAll("\\/", "\\\\");
			break;
		default:
			break;
		}
		
		if (path.length() > 2 && path.substring(0,3).matches("^\\\\\\S\\:$")) {
			path = path.substring(1);
		}
		return path;
	}
	
	/**
	 * append参数到url,不会出现两个?或者没有?
	 * @param url
	 * @param param
	 * @return
	 */
	public static String appendParam(String url,Map<String, Object> param){
		StringBuffer sb = new StringBuffer(url);
		if (url.contains("?")) {
			sb.append("&");
		}else {
			sb.append("?");
		}
		for (Entry<String, Object> temp:param.entrySet()) {
			sb.append(temp.getKey());
			sb.append("=");
			sb.append(temp.getValue());
			sb.append("&");
		}
		String string = sb.toString();
		if (string.endsWith("&")) {
			string = string.substring(0, string.length()-1);
		}
		return string;
	}
	
}
