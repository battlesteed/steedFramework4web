package steed.util.system;

import java.lang.reflect.Array;

import steed.util.reflect.ReflectUtil;

public class StringSplitUtil {
	public static <T> String getString(T[] t){
		return getString(t, new StringSplitSupport<T>() {
			@Override
			public String getString(T t) {
				return t.toString();
			}
		});
	}
	public static <T> String getString(T[] t,StringSplitSupport<T> splitSupport){
		if (t != null) {
			StringBuffer sb = new StringBuffer();
			if (t.length > 0) {
				sb.append(splitSupport.getString(t[0]));
			}
			for(int i = 1; i<t.length;i++){
				sb.append(",");
				sb.append(splitSupport.getString(t[i]));
			}
			return sb.toString();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] convertFromString(String[] ids,Class<T> type){
		T[] newInstance = (T[]) Array.newInstance(type, ids.length);
		for (int i = 0; i < ids.length; i++) {
			newInstance[i] = (T) ReflectUtil.string2BaseID(type, ids[i]);
		}
		return newInstance;
	}
	
	public interface StringSplitSupport<T>{
		public String getString(T t);
	}
}
