package steed.engine.task;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import steed.util.base.BaseUtil;
import steed.util.file.FileUtil;
import steed.util.system.SimpleTaskEngine;

/**
 * 清理日志引擎
 * @author 战马
 *
 */
public class DeleteLogEngine extends SimpleTaskEngine{
	/**
	 * 日志目录,自动获取,不需要手动设置
	 */
	private String log_dir;
	/**
	 * 日志保留时间,-1表示永不过期,单位秒(默认30天)
	 */
	private long remainTime = 60*60*24*30;
	/**
	 * 硬盘最少要留下的空间大小,-1表示不留任何空间,单位MB.(如果空间不够,即使日志没过期,也会按照从新到旧的顺序删除日志,直到日志删除完或者空间大小足够为止)
	 * 默认留2GB
	 */
	private long minSpaceRemain = 2048;
	
	public DeleteLogEngine(){
		log_dir = System.getProperty("log_dir");
	}
	
	@Override
	public void doTask() {
		BaseUtil.getLogger().debug("开始清理日志任务.....");
		List<File> allFile = new FileUtil().getAllFile(log_dir, null);
		if (remainTime >= 0) {
			deleteOverdueFile(allFile);
		}
		keepSpaceSave(allFile);
	}

	protected void keepSpaceSave(List<File> allFile) {
		long freeSpace = new File(log_dir).getUsableSpace();
		if (freeSpace < minSpaceRemain*1024*1024) {
			BaseUtil.getLogger().warn("硬盘空间不足{}MB,开始删除未过期日志!!!",minSpaceRemain);
			long spaceNeedToDelete = minSpaceRemain*1024*1024 - freeSpace;
			
			Collections.sort(allFile, new Comparator<File>() {
				@Override
				public int compare(File o1, File o2) {
					return (int) (o1.lastModified() - o2.lastModified());
				}
			});
			
			for(File temp:allFile){
				if (spaceNeedToDelete < 0) {
					BaseUtil.getLogger().debug("日志删除完毕,硬盘剩余空间已达安全值");
					break;
				}
				spaceNeedToDelete -= temp.length();
				temp.delete();
				BaseUtil.getLogger().warn("删除未过期日志---->{}",temp.getAbsolutePath());
			}
			
			if (spaceNeedToDelete > 0) {
				BaseUtil.getLogger().error("日志全部删除也无法保证硬盘剩余空间达到安全值,请处理!!!!");
			}
		}
	}
	
	

	private void deleteOverdueFile(List<File> allFile) {
		long now = new Date().getTime();
		Iterator<File> iterator = allFile.iterator();
		while (iterator.hasNext()) {
			File next = iterator.next();
			if(now > next.lastModified() + remainTime*1000){
				BaseUtil.getLogger().debug("删除过期日志---->{}",next.getAbsolutePath());
				next.delete();
				iterator.remove();
			}
		}
	}

}
