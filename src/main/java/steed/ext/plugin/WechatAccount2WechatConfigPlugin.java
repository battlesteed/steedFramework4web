package steed.ext.plugin;

import steed.domain.wechat.WechatAccount;
import steed.domain.wechat.WechatConfig;
import steed.domain.wechat.WechatMerchant;

public class WechatAccount2WechatConfigPlugin {
	public WechatConfig getWechatConfig(WechatAccount wechatAccount){
		WechatConfig wechatConfig = new WechatConfig();
		wechatConfig.setAppID(wechatAccount.getAppID());
		wechatConfig.setAppSecret(wechatAccount.getAppSecret());
		wechatConfig.setEncodingAESKey(wechatAccount.getEncodingAESKey());
		wechatConfig.setToken(wechatAccount.getToken());
		WechatMerchant wechatMerchant = new WechatMerchant();
		wechatMerchant.setCertPath(wechatAccount.getMerchantCertPath());
		wechatMerchant.setId(wechatAccount.getMerchantId());
		wechatMerchant.setKey(wechatAccount.getMerchantKey());
		wechatConfig.setWechatMerchant(wechatMerchant);
		return wechatConfig;
	}
	
	public WechatAccount getWechatAccount(WechatConfig wechatConfig){
		WechatAccount wechatAccount = new WechatAccount();
		wechatAccount.setAppID(wechatConfig.getAppID());
		wechatAccount.setAppSecret(wechatConfig.getAppSecret());
		wechatAccount.setEncodingAESKey(wechatConfig.getEncodingAESKey());
		wechatAccount.setToken(wechatConfig.getToken());
		WechatMerchant wechatMerchant = wechatConfig.getWechatMerchant();
		wechatAccount.setMerchantId(wechatMerchant.getId());
		wechatAccount.setMerchantKey(wechatMerchant.getKey());
		wechatAccount.setMerchantCertPath(wechatAccount.getMerchantCertPath());
		return wechatAccount;
	}
}
