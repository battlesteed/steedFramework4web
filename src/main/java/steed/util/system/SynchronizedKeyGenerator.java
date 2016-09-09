package steed.util.system;

import java.util.UUID;

import steed.util.base.BaseUtil;

public class SynchronizedKeyGenerator {
	
	public static String getKey(String prefix,Object key){
		synchronized (prefix) {
			BaseUtil.getLogger().debug("获取{}的同步ID",prefix);
			String prefix2 = "SynchronizedKeyGenerator_"+prefix;
			String data = (String) DataCacheUtil.getData(key, prefix2);
			if (data == null) {
				data = UUID.randomUUID().toString();
			}
			DataCacheUtil.setData(key, prefix2, data, 1000*60*10L);
			return data;
		}
	}
}
