package steed.util.other;

public class ClassUtil {
	/**
	 * 获取类名简写
	 * @param t
	 * @return
	 */
	public String getClassSimpleName(Class t){
		String name = t.getName();
		return name.substring(name.lastIndexOf(".")+1);
	}
}
