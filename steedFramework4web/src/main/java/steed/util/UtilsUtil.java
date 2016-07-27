package steed.util;

import steed.util.base.PathUtil;
import steed.util.base.PropertyUtil;
import steed.util.dao.DaoUtil;
import steed.util.dao.HibernateUtil;
import steed.util.wechat.MutiAccountSupportUtil;

/**
 * 工具类的工具类
 * @author 战马
 *
 */
public class UtilsUtil {
	/**
	 * 初始化所有工具类,使用工具类前请先调用该方法
	 */
	public static void initUtils(){
		/**
		 * 设置log4j日志目录。
		 */
		System.setProperty("log_dir", PathUtil.praseRelativePath(PropertyUtil.getConfig("log_dir")));
		//log4j日志文件编码
		System.setProperty("log_encoding",PropertyUtil.getConfig("characterSet"));
		MutiAccountSupportUtil.setSingleMode(PropertyUtil.getBoolean("wechat.singleMode"));
//		HibernateUtil.setWhole_closeSession(PropertyUtil.getConstant("whole_closeSession",Boolean.class));
	}
	
	/**
	 * 释放工具类资源
	 */
	public static void releaseUtils(){
		HibernateUtil.release();
		MutiAccountSupportUtil.relese();
		DaoUtil.relese();
	}
}
