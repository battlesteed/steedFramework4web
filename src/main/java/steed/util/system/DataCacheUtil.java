package steed.util.system;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import steed.util.base.BaseUtil;

/**
 * 缓存工具类
 * @author 战马
 *
 */
public class DataCacheUtil {
	private static Map<String, Object> dataMap = new HashMap<String, Object>();
	private static Map<String, Long> timeMap = new HashMap<String, Long>();
	
	static{
			new TaskEngine() {
				@Override
				public void doTask() {
					BaseUtil.getLogger().debug("开始清理缓存数据");
					Map<String, Object> tempDataMap = new HashMap<String, Object>();
					Map<String, Long> tempTimeMap = new HashMap<String, Long>();
					long current = new Date().getTime();
					for(Entry<String, Long> temp:timeMap.entrySet()){
						if (current < temp.getValue()) {
							String key = temp.getKey();
							tempTimeMap.put(key, temp.getValue());
							tempDataMap.put(key, dataMap.get(key));
						}
					}
					dataMap = tempDataMap;
					timeMap = tempTimeMap;
				}
				
				@Override
				protected void startUp(ScheduledExecutorService scheduledExecutorService) {
					scheduledExecutorService.scheduleWithFixedDelay(this, 1, 1, TimeUnit.HOURS);
				}
				
			}.start();
	}
	
	public static Object getData(Object key,String prifix){
		String key2 = key.toString()+"_steed_dataCache_"+prifix;
		Long time = timeMap.get(key2);
		if (time != null && new Date().getTime() > time) {
			return null;
		}
		return dataMap.get(key2);
	}
	public static void setData(Object key,String prifix,Object value){
		String key2 = key.toString()+"_steed_dataCache_"+prifix;
		dataMap.put(key2, value);
		timeMap.put(key2,new Date().getTime() + 1000*60*60*2);
	}
	public static void setData(Object key,String prifix,Object value,Long lifeTime){
		String key2 = key.toString()+"_steed_dataCache_"+prifix;
		dataMap.put(key2, value);
		timeMap.put(key2,new Date().getTime()+lifeTime);
	}
	
}
