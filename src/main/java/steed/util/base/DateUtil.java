package steed.util.base;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateUtil {
	private static Map<String, SimpleDateFormat> dateFormatCache = new HashMap<String, SimpleDateFormat>();
	/**
	 * 获取今天 00:00分的date
	 * 
	 */
	public static Date getToday(){
		Date now = new Date();
		return getToday(now);
	}
	
	private static SimpleDateFormat getDateFormat(String pattern){
		SimpleDateFormat temp = dateFormatCache.get(pattern);
		if (temp == null) {
			temp = new SimpleDateFormat(pattern);
			dateFormatCache.put(pattern, temp);
		}
		return temp;
	}
	
	public static boolean isToday(Date date){
		Date today = getToday(date);
		return today.getTime() == getToday().getTime();
	}
	public static Date parseDate(String source,String pattern){
		try {
			return getDateFormat(pattern).parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 获取距离date前lastCount天的data
	 * @param lastCount 可以是负数
	 * @return
	 */
	public static Date getLastday(int lastCount,Date date){
		return new Date(date.getTime()-lastCount*getOneDateMillisecond());
	}
	/**
	 * 获取一天所占的毫秒数
	 * @return
	 */
	public static long getOneDateMillisecond(){
		return 1000*60*60*24;
	}
	public static String getStringFormatDate(Date date,String pattern){
		return getDateFormat(pattern).format(date);
	}
	/**
	 * 获取now的那天 00:00分的date
	 * @param now
	 * @return
	 */
	public static Date getToday(Date now){
		@SuppressWarnings("deprecation")
		Date today = new Date(now.getYear(), now.getMonth(),now.getDate()); 
		return today;
	}
	/**
	 * 
	 * @param date
	 * @return 距离date过去的分钟数
	 */
	public static int getLastXmin(Date date){
		return (int) Math.abs((new Date().getTime()-date.getTime())/(1000*60));
	}
	/**
	 * 
	 * @param date
	 * @return date比date2多的天数
	 */
	public static int getLastDate(Date date,Date date2){
		return (int) ((date.getTime()-date2.getTime())/(1000*60*60*24));
	}
	
	/**
	 * 获取距离beginDate mini分钟的date
	 * @param beginDate
	 * @param mini 可以是负数
	 * @return
	 */
	public static Date getLastXminiDate(Date beginDate,float mini){
		Date date = new Date(beginDate.getTime() - (long)(mini*1000*60));
		return date;
	}
}
