package steed.util.system;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import steed.exception.runtime.system.SystemInitException;
import steed.util.base.BaseUtil;

public abstract class SimpleTaskEngine extends TaskEngine{
	/**
	 * 执行间隔
	 */
	private long delay;
	/**
	 * 第一次执行时的时间间隔
	 */
	private long initialDelay;
	/**
	 * 时间间隔的单位,见java.util.concurrent.TimeUnit或steed.util.system.SimpleTaskEngine.getTimeUnit(String)
	 */
	private String timeUnit;
	/**
	 * 0,schedule(只执行一次);
	 * 1,scheduleWithFixedDelay(间隔固定的时间执行);
	 * 2,scheduleAtFixedRate(本任务执行完成后,延迟固定时间再执行下一次任务)
	 * 不明白的朋友可以百度一下scheduleWithFixedDelay和scheduleAtFixedRate的区别.
	 * 
	 * 在下在这里用一句简单明了的话说一下吧:scheduleWithFixedDelay是从任务开始执行就开始计算执行间隔时间了,
	 * scheduleAtFixedRate是从任务执行完才开始计算执行时间间隔.
	 */
	private int startType = 1;
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
