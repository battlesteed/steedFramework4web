package steed.util.wechat.domain.sys;

import steed.util.base.PathUtil;
import steed.util.base.PropertyUtil;
import steed.util.digest.AESUtil;

public class WechatMerchant {
	
	private String key;
	private String id;
	/**
	 * 证书路径
	 */
	private String certPath;
	/**
	 * 从配置文件加载appID等
	 */
	public void loadAttrFromConfig(){
		key =  AESUtil.aesDecode(PropertyUtil.getConfig("wechat.merchant.key"));
		id =  PropertyUtil.getConfig("wechat.merchant.id");
		certPath = PathUtil.praseRelativePath(PropertyUtil.getConfig("wechat.merchant.certPath"));
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getCertPath() {
		return certPath;
	}

	public void setCertPath(String certPath) {
		this.certPath = certPath;
	}

	public WechatMerchant() {
		super();
	}
	
}
