package steed.util.system;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import steed.domain.system.Config;
import steed.exception.runtime.system.FrameworkException;
import steed.util.base.DateUtil;
import steed.util.dao.DaoUtil;



/**
 * @author 战马
 *
 */
public class FlowUtil {
//	private static List<String> keyList;
//	private static final long sleepTime = 1000*60*10;
//	private static Date lastUpdateDate;
	private FlowUtil(){}
	/*
	static{
		keyList = new ArrayList<String>();
		
		new Thread(new Runnable() {
			@Override
			public synchronized void run() {
				while(true){
					try {
						Thread.sleep(sleepTime);
						Date today = DateUtil.getToday();
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						Config config = DaoUtil.get(Config.class, "lastSummaryDate");
						lastUpdateDate = dateFormat.parse(config.getValue());
						if (lastUpdateDate.getTime() < today.getTime()) {
							Config config2 = new Config();
							config2.setValue(dateFormat.format(today));
							for(String key:keyList){
								config2.setKee(key);
								DaoUtil.update(config);
							}
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		
	}*/
	/**
	 * 
	 * @param key
	 * @param length
	 * @param includeTimestamp
	 * @return
	 */
	public static String getFlowString(String key,
			int flowLength,boolean includeTimestamp){
		synchronized (key) {
			Config config2 = DaoUtil.get(Config.class, key);
			if (config2 == null) {
				config2 = new Config(key, "201603140");
				config2.save();
			}
			Config config = updateFlow(config2,includeTimestamp);
			String flow = config.getValue().substring(8);
			for (int i = flow.length(); i < flowLength; i++) {
				flow = "0"+flow;
			}
			if (includeTimestamp) {
				flow = config.getValue().substring(0, 8)+flow;
			}
			return flow;
		}
	}

	private static Config updateFlow(Config config,boolean includeTimestamp) {
		String dateString = config.getValue().substring(0, 8);
		Date today = DateUtil.getToday();
		String flow = config.getValue().substring(8);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Date lastUpdateDate;
		try {
			lastUpdateDate = dateFormat.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new FrameworkException("表config中主键为"+config.getKee()+
					"的值"+dateString+"不符合yyyyMMdd日期格式！！");
		}
		if (lastUpdateDate.getTime() < today.getTime()) {
			if (includeTimestamp) {
				config.setValue(dateFormat.format(today)+1);
			}else {
				config.setValue(dateFormat.format(today)+(Long.parseLong(flow)+1));
			}
		}else {
			config.setValue(dateString+(Long.parseLong(flow)+1));
		}
		config.update();
		return config;
	} 
}
