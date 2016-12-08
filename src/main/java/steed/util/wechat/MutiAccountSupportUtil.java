package steed.util.wechat;

import steed.domain.wechat.WechatAccount;
import steed.hibernatemaster.util.DaoUtil;
import steed.util.base.ContextUtil;

/**
 * 微信多账号支持工具类
 * @author 战马
 *
 */
public class MutiAccountSupportUtil {
	private static final ThreadLocal<WechatAccount> wechatAccountThreadLocal = new ThreadLocal<WechatAccount>();
	/**
	 * 单账号模式
	 */
	private static boolean singleMode = true;
	
	private static MutiAccountSupport mutiAccountSupport = new MutiAccountSupport() {
		
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

	public static void setWechatAccount(WechatAccount wechatAccount) {
		wechatAccountThreadLocal.set(wechatAccount);
	}
	
	public static void relese(){
		wechatAccountThreadLocal.remove();
	}
	
	public static WechatAccount getWechatAccount(){
		if (wechatAccountThreadLocal.get() != null) {
			return wechatAccountThreadLocal.get();
		}
		if (singleMode) {
			WechatAccount wechatConfig = new WechatAccount();
			wechatConfig.loadAttrFromConfig();
			return wechatConfig;
		}else {
			return mutiAccountSupport.getWechatAccount();
		}
	}
	
	public static WechatAccount getWechatAccount(String appID){
		if (singleMode) {
			WechatAccount wechatConfig = new WechatAccount();
			wechatConfig.loadAttrFromConfig();
			return wechatConfig;
		}else {
			return mutiAccountSupport.getWechatAccount(appID);
		}
	}
	
	public interface MutiAccountSupport{
		public WechatAccount getWechatAccount();
		
		public WechatAccount getWechatAccount(String appID);
		
		public void setWechatAccount(WechatAccount wechatAccount);
	}
	
	
}
