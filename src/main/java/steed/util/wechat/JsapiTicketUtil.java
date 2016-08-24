package steed.util.wechat;

import java.util.Date;

import org.apache.log4j.Logger;

import steed.util.base.StringUtil;
import steed.util.system.DataCacheUtil;
import steed.util.wechat.domain.result.JsapiTicket;

/**
 * JsapiTicket工具类，若想获取JsapiTicket必须使用该类的getJsapiTicket方法，
 * 不允许另写方法。
 * @author 战马
 *
 */
public class JsapiTicketUtil {
	private static Logger logger = Logger.getLogger(JsapiTicketUtil.class);
	private static JsapiTicket jsapiTicket = null;
	
	/**
	 * access_token是否还有效
	 * @return access_token是否还有效
	 */
	private static boolean isJsapiTicketValid(JsapiTicket data){
		if (data.getExpires_in() <= 0) {
			return false;
		}else {
			long timePastAfterGetAccessToken = new Date().getTime() - data.getAccess_token_getTime();
			/**
			 * 这里允许有15分钟的误差
			 */
			if (timePastAfterGetAccessToken < (data.getExpires_in()-60*15)*1000) {
				return true;
			}else {
				return false;
			}
		}
	}
	

	/**
	 * 获取未过期的JsapiTicket
	 * @return 未过期的JsapiTicket,如失败则steed.util.wechat.domain.result.JsapiTicket.ticket值为null
	 */
	@SuppressWarnings("deprecation")
	public static JsapiTicket getJsapiTicket() {
		if (MutiAccountSupportUtil.isSingleMode()) {
			return jsapiTicket = JsapiTicketUtil.getJsapiTicket(jsapiTicket);
		}else {
			String appID = MutiAccountSupportUtil.getWechatConfig().getAppID();
			JsapiTicket jsapiTicket2 = getJsapiTicket((JsapiTicket) DataCacheUtil.getData(appID, "wechatJsapiTicket"));
			DataCacheUtil.setData(appID, "wechatJsapiTicket", jsapiTicket2);
			return jsapiTicket2;
		}
	}
	
	/**
	 * 获取未过期的JsapiTicket,本工具类已经实现了JsapiTicket缓存，一般不用该方法，请用getJsapiTicket()
	 * @param data 之前的JsapiTicket，为null时将重新获取AccessToken.access_token值为null也会重新获取
	 * @return 未过期的access_token,如失败则AccessToken.access_token值为null
	 */
	private static JsapiTicket getJsapiTicket(JsapiTicket data){
		if (data == null) {
			data = new JsapiTicket("", 0, 0);
		}
		if (!isJsapiTicketValid(data) || StringUtil.isStringEmpty(data.getTicket())) {
			logger.debug("JsapiTicket过期，重新获取");
			return WechatInterfaceInvokeUtil.getJsapiTicket();
		}
		return data;
	}
	
	
	
	
}
