package steed.util.system;

import java.util.concurrent.ScheduledExecutorService;

public abstract class TaskEngine implements Runnable{
	

	/**
	 * 在这里写定时任务的启动方式,包括执行间隔,执行模式等
	 * 例子:
	 * 	//只执行一次,请调用该方法
		scheduledExecutorService.schedule(this, 10, TimeUnit.SECONDS);
		//执行多次,请调用下面其中一种方法
		scheduledExecutorService.scheduleWithFixedDelay(this, 0, 10, TimeUnit.MINUTES);
		scheduledExecutorService.scheduleAtFixedRate(this, 0, 10, TimeUnit.MINUTES);
	 */
	protected abstract void startUp(ScheduledExecutorService scheduledExecutorService);
	
	public void start(){
		startUp(TaskUtil.getScheduledexecutorservice());
	}
}
