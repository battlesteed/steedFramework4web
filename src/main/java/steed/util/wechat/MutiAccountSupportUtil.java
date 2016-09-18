package steed.util.wechat;

import steed.domain.wechat.WechatAccount;
import steed.ext.plugin.WechatAccount2WechatConfigPlugin;
import steed.util.base.ContextUtil;
import steed.util.dao.DaoUtil;
import steed.util.wechat.domain.sys.WechatConfig;

/**
 * 微信多账号支持工具类
 * @author 战马
 *
 */
public class MutiAccountSupportUtil {
	private static final ThreadLocal<WechatConfig> threadLocal = new ThreadLocal<WechatConfig>();
	private static final ThreadLocal<WechatAccount> wechatAccountThreadLocal = new ThreadLocal<WechatAccount>();
	/**
	 * 单账号模式
	 */
	private static boolean singleMode = true;
	
	private static MutiAccountSupport mutiAccountSupport = new MutiAccountSupport() {
		
		@Override
		public void setWechatConfig(WechatConfig wechatConfig) {
			ContextUtil.getSession().setAttribute("wechatConfig",wechatConfig);
			ContextUtil.getSession().setAttribute("wechatAccount",new WechatAccount2WechatConfigPlugin().getWechatAccount(wechatConfig));
		}
		
		@Override
		public WechatConfig getWechatConfig() {
			WechatConfig attribute = (WechatConfig) ContextUtil.getSession().getAttribute("wechatConfig");
			return attribute;
		}

		@Override
		public WechatConfig getWechatConfig(String appID) {
			return new WechatAccount2WechatConfigPlugin().getWechatConfig(DaoUtil.get(WechatAccount.class, appID));
		}

		@Override
		public WechatAccount getWechatAccount() {
			return (WechatAccount) ContextUtil.getSession().getAttribute("wechatAccount");
		}

		@Override
		public WechatAccount getWechatAccount(String appID) {
			return DaoUtil.get(WechatAccount.class, appID);
		}

		@Override
		public void setWechatAccount(WechatAccount wechatAccount) {
			ContextUtil.getSession().setAttribute("wechatAccount",wechatAccount);
			ContextUtil.getSession().setAttribute("wechatConfig",new WechatAccount2WechatConfigPlugin().getWechatConfig(wechatAccount));
		}
	};
	
	
	public static boolean isSingleMode() {
		return singleMode;
	}
	
	public static void setSingleMode(boolean singleMode) {
		MutiAccountSupportUtil.singleMode = singleMode;
	}
	
	public static MutiAccountSupport getMutiAccountSupport() {
		return mutiAccountSupport;
	}

	public static void setMutiAccountSupport(MutiAccountSupport mutiAccountSupport) {
		MutiAccountSupportUtil.mutiAccountSupport = mutiAccountSupport;
	}

	public static void setWechatConfig(WechatConfig wechatConfig) {
		threadLocal.set(wechatConfig);
		wechatAccountThreadLocal.set(new WechatAccount2WechatConfigPlugin().getWechatAccount(wechatConfig));
	}
	public static void setWechatAccount(WechatAccount wechatAccount) {
		wechatAccountThreadLocal.set(wechatAccount);
		threadLocal.set(new WechatAccount2WechatConfigPlugin().getWechatConfig(wechatAccount));
	}
	
	public static void relese(){
		threadLocal.remove();
		wechatAccountThreadLocal.remove();
	}
	/**
	 * @see #getWechatAccount
	 * @return
	 */
	@Deprecated
	public static WechatConfig getWechatConfig(){
		if (threadLocal.get() != null) {
			return threadLocal.get();
		}
		if (singleMode) {
			WechatConfig wechatConfig = new WechatConfig();
			wechatConfig.loadAttrFromConfig();
			return wechatConfig;
		}else {
			return mutiAccountSupport.getWechatConfig();
		}
	}
	public static WechatAccount getWechatAccount(){
		if (wechatAccountThreadLocal.get() != null) {
			return wechatAccountThreadLocal.get();
		}
		if (singleMode) {
			WechatConfig wechatConfig = new WechatConfig();
			wechatConfig.loadAttrFromConfig();
			return new WechatAccount2WechatConfigPlugin().getWechatAccount(wechatConfig);
		}else {
			return mutiAccountSupport.getWechatAccount();
		}
	}
	/**
	 * @see #getWechatAccount
	 * @param appID
	 * @return
	 */
	@Deprecated
	public static WechatConfig getWechatConfig(String appID){
		if (singleMode) {
			WechatConfig wechatConfig = new WechatConfig();
			wechatConfig.loadAttrFromConfig();
			return wechatConfig;
		}else {
			return mutiAccountSupport.getWechatConfig(appID);
		}
	}
	
	public static WechatAccount getWechatAccount(String appID){
		if (singleMode) {
			WechatConfig wechatConfig = new WechatConfig();
			wechatConfig.loadAttrFromConfig();
			return new WechatAccount2WechatConfigPlugin().getWechatAccount(wechatConfig);
		}else {
			return mutiAccountSupport.getWechatAccount(appID);
		}
	}
	
	public interface MutiAccountSupport{
		/**
		 * 过时,
		 * @see #getWechatAccount
		 * @return
		 */
		@Deprecated
		public WechatConfig getWechatConfig();
		public WechatAccount getWechatAccount();
		/**
		 * 过时,
		 * @see #getWechatAccount
		 * @return
		 */
		@Deprecated
		public WechatConfig getWechatConfig(String appID);
		
		public WechatAccount getWechatAccount(String appID);
		/**
		 * 过时,
		 * @see #setWechatAccount
		 * @return
		 */
		@Deprecated
		public void setWechatConfig(WechatConfig wechatConfig);
		
		public void setWechatAccount(WechatAccount wechatAccount);
	}
	
	
}
