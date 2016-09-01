package steed.util.system;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import steed.exception.runtime.system.SystemInitException;
import steed.util.base.BaseUtil;

public abstract class SimpleTaskEngine extends TaskEngine{
	private long delay;
	private long initialDelay;
	private String timeUnit;
	/**
	 * 0,schedule;1,scheduleWithFixedDelay;2,scheduleAtFixedRate
	 */
	private int startType = 0;
	/**
	 * 在这里写定时任务的启动方式,包括执行间隔,执行模式等
	 * 例子:
	 * 	//只执行一次,请调用该方法
		scheduledExecutorService.schedule(this, 10, TimeUnit.SECONDS);
		//执行多次,请调用下面其中一种方法
		scheduledExecutorService.scheduleWithFixedDelay(this, 0, 10, TimeUnit.MINUTES);
		scheduledExecutorService.scheduleAtFixedRate(this, 0, 10, TimeUnit.MINUTES);
	 */
	protected void startUp(ScheduledExecutorService scheduledExecutorService){
		switch (startType) {
			case 0:
				scheduledExecutorService.schedule(this, delay, getTimeUnit(timeUnit));
				break;
			case 1:
				scheduledExecutorService.scheduleWithFixedDelay(this, initialDelay, delay, getTimeUnit(timeUnit));
				break;
			case 2:
				scheduledExecutorService.scheduleAtFixedRate(this, initialDelay, delay, getTimeUnit(timeUnit));
				break;
			default:
				BaseUtil.getLogger().warn("不支持值为"+startType+"的startType!");
				break;
		}
	}
	private TimeUnit getTimeUnit(String timeUint){
		// 蛋疼╭∩╮（︶︿︶）╭∩╮鄙视你！，为兼容jdk1.6又不能用switch
		String upperCase = timeUint.toUpperCase();
		if("DAYS".equals(upperCase)){
			return TimeUnit.DAYS;
		}else if("HOURS".equals(upperCase)){
			return TimeUnit.HOURS;
		}else if("MICROSECONDS".equals(upperCase)){
			return TimeUnit.MICROSECONDS;
		}else if("MILLISECONDS".equals(upperCase)){
			return TimeUnit.MILLISECONDS;
		}else if("MINUTES".equals(upperCase)){
			return TimeUnit.MINUTES;
		}else if("NANOSECONDS".equals(upperCase)){
			return TimeUnit.NANOSECONDS;
		}else if("SECONDS".equals(upperCase)){
			return TimeUnit.SECONDS;
		}else {
			throw new SystemInitException("在java.util.concurrent.TimeUnit中没有"+timeUint+"类型的枚举变量");
		}
	}
}
