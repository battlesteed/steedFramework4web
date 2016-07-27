package steed.domain.wechat;

import steed.util.base.PropertyUtil;
import steed.util.digest.AESUtil;

public class WechatConfig {
	
	private String appID;
	private String token;
	private String appSecret;
	private String encodingAESKey;
	private WechatMerchant wechatMerchant;
	/**
	 * 从配置文件加载appID等
	 */
	public void loadAttrFromConfig(){
		appID = PropertyUtil.getConfig("wechat.appID");
		token = AESUtil.aesDecode(PropertyUtil.getConfig("wechat.token"));
		appSecret =  AESUtil.aesDecode(PropertyUtil.getConfig("wechat.appSecret"));
		encodingAESKey =  AESUtil.aesDecode(PropertyUtil.getConfig("wechat.encodingAESKey"));
		wechatMerchant = new WechatMerchant();
		wechatMerchant.loadAttrFromConfig();
	}
	
	public WechatMerchant getWechatMerchant() {
		return wechatMerchant;
	}

	public void setWechatMerchant(WechatMerchant wechatMerchant) {
		this.wechatMerchant = wechatMerchant;
	}

	public WechatConfig() {
		super();
	}
	public WechatConfig(String appID) {
		super();
		this.appID = appID;
	}

	public String getAppID() {
		return appID;
	}

	public String getEncodingAESKey() {
		return encodingAESKey;
	}

	public void setEncodingAESKey(String encodingAESKey) {
		this.encodingAESKey = encodingAESKey;
	}

	public void setAppID(String appID) {
		this.appID = appID;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getToken() {
		return token;
	}
	
}
