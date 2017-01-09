package steed.util.wechat;

import java.util.Date;

import org.apache.log4j.Logger;

import steed.util.base.PropertyUtil;
import steed.util.base.StringUtil;
import steed.util.system.DataCacheUtil;
import steed.util.system.SynchronizedKeyGenerator;
import steed.util.wechat.domain.result.AccessToken;

/**
 * access_token工具类，若想获取access_token必须使用该类的getAccessToken方法，
 * 不允许另写方法。
 * @author 战马
 *
 */
public class AccessTokenUtil {
	private static Logger logger = Logger.getLogger(AccessTokenUtil.class);
	private static AccessToken accessToken = null;
	/**
	 * #微信token提前获取的多少时间获取,单位秒<br>
#例如,token有效期是2小时,当前配置是在过期前的15分钟刷新token<br>
#利用该配置和微信刷新token后原token还有一段时间可以继续使用,<br>
#可以配置成提前115(具体数值根据情况调整)分钟刷新,<br>
#这样可以达到同时在多个程序里面跑,而不会因为相互独立刷新token导致微信接口调用失败的问题<br>
#但是请注意不要刷新太频繁,这样会导致刷新toke接口超过调用次数限制.<br>
	 */
	public static int tokenAdvanceRefreshTime = PropertyUtil.getInteger("wechat.tokenAdvanceRefreshTime");
	
	/**
	 * access_token是否还有效
	 * @return access_token是否还有效
	 */
	private static boolean isAccessTokenValid(AccessToken data){
		if (data.getExpires_in() <= 0) {
			return false;
		}else {
			long timePastAfterGetAccessToken = new Date().getTime() - data.getAccess_token_getTime();
			if (timePastAfterGetAccessToken < (data.getExpires_in()-tokenAdvanceRefreshTime)*1000) {
				return true;
			}else {
				return false;
			}
		}
	}
	

	/**
	 * 获取未过期的access_token
	 * @return 未过期的access_token,如失败则AccessToken.access_token值为null
	 */
	public static AccessToken getAccessToken() {
		String appID = MutiAccountSupportUtil.getWechatAccount().getAppID();
		synchronized(SynchronizedKeyGenerator.getKey("steed.util.wechat.AccessTokenUtil.getAccessToken()", appID)){
			if (MutiAccountSupportUtil.isSingleMode()) {
				return accessToken = AccessTokenUtil.getAccessToken(accessToken);
			}else {
				AccessToken accessToken2 = getAccessToken((AccessToken) DataCacheUtil.getData(appID, "wechatAccessToken"));
				DataCacheUtil.setData(appID, "wechatAccessToken", accessToken2);
				return accessToken2;
			}
		}
	}
	
	/**
	 * 获取未过期的access_token,本工具类已经实现了access_token缓存，一般不用改方法，请用getAccessToken()
	 * @param data 之前的AccessToken，为null时将重新获取AccessToken.access_token值为null也会重新获取
	 * @return 未过期的access_token,如失败则AccessToken.access_token值为null
	 */
	private static AccessToken getAccessToken(AccessToken data){
		if (data == null) {
			data = new AccessToken("", 0, 0);
		}
		if (!isAccessTokenValid(data) || StringUtil.isStringEmpty(data.getAccess_token())) {
			logger.debug("access_token过期，重新获取");
			return WechatInterfaceInvokeUtil.getAccessToken();
		}
		return data;
	}
	
}
