package steed.util.system;

import steed.util.base.BaseUtil;

public abstract class SimpleTaskEngine implements Runnable{
	protected boolean loop = true;
	@Override
	public void run() {
		try{
			doTask();
		}catch (Exception e) {
			BaseUtil.getLogger().warn("后台任务出错...",e);
		}finally {
			if (loop) {
				TaskUtil.startTask(this.getClass());
			}
		}
	}
	
	public boolean isLoop() {
		return loop;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	/**
	 * 在这里写定时任务
	 */
	protected abstract void doTask();

}
