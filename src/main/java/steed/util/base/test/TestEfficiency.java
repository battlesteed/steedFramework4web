package steed.util.base.test;

import java.util.Date;
import java.util.List;

import steed.util.base.BaseUtil;

/**
 * 用于测试代码运行效率
 * @author 战马
 *
 */
public class TestEfficiency {
	private Date begin;
	private Date end;
	private List<String> recordList;
	/**
	 * 记录开始时间
	 */
	public Date begin(){
		begin = new Date();
		return begin;
	}
	/**
	 * 记录结束时间
	 */
	public Date end(){
		end = new Date();
		return end;
	}
	/**
	 * 打印耗时
	 * @param prefix 为空时使用默认值“用时”
	 * @return
	 */
	public String outUsedTime(String prefix){
		if (prefix == null) {
			prefix = "用时";
		}
		return BaseUtil.out(getUseTime()+"毫秒", prefix);
	}
	/**
	 * 记录耗时
	 */
	public String record(String prefix){
		if (prefix == null) {
			prefix = "用时";
		}
		String record = prefix+"------------>"+getUseTime()+"毫秒";
		recordList.add(record);
		return record;
	}
	/**
	 * 打印记录
	 */
	public void printRecord(){
		for(String str:recordList){
			System.out.println(str);
		}
	}
	
	public long getUseTime() {
		return end.getTime() - begin.getTime();
	}
	/**
	 * 记录结束时间并打印耗时
	 */
	public String endAndOutUsedTime(String prefix){
		end = new Date();
		return outUsedTime(prefix);
	}
}
