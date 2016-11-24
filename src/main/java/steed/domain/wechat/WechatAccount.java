package steed.domain.wechat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import steed.domain.BaseRelationalDatabaseDomain;
import steed.ext.domain.user.User;
import steed.util.base.PathUtil;
import steed.util.base.PropertyUtil;
import steed.util.digest.AESUtil;


/** 微信公众号 */
@Entity
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class WechatAccount extends BaseRelationalDatabaseDomain{
	private static final long serialVersionUID = -703139000141520185L;
	/** AppSecret */
	private java.lang.String appSecret;
	private java.lang.String appID;
	/** 公众号所属的用户 */
//	private java.lang.String user_nickName;
	/** Token */
	private java.lang.String token;
	/** EncodingAESKey */
	private java.lang.String encodingAESKey;
	/** 公众账号名 */
	private java.lang.String accountName;
	/** 头像url */
	private java.lang.String headImg;
	/** 账号类型,0订阅号,1服务号,2企业号 
	 * */
	private Integer accountType;
	/** 公众号所属的用户 */
	private User user;
	
	private String merchantKey;
	private String merchantId;
	/**
	 * 证书路径
	 */
	private String merchantCertPath;
	
	
	/**
	 * 从配置文件加载appID等
	 */
	public void loadAttrFromConfig(){
		appID = PropertyUtil.getConfig("wechat.appID");
		token = AESUtil.aesDecode(PropertyUtil.getConfig("wechat.token"));
		appSecret =  AESUtil.aesDecode(PropertyUtil.getConfig("wechat.appSecret"));
		encodingAESKey =  AESUtil.aesDecode(PropertyUtil.getConfig("wechat.encodingAESKey"));
		merchantKey =  AESUtil.aesDecode(PropertyUtil.getConfig("wechat.merchant.key"));
		merchantId =  PropertyUtil.getConfig("wechat.merchant.id");
		merchantCertPath = PathUtil.praseRelativePath(PropertyUtil.getConfig("wechat.merchant.certPath"));
	}

	public String getMerchantKey() {
		return merchantKey;
	}
	public void setMerchantKey(String merchantKey) {
		this.merchantKey = merchantKey;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getMerchantCertPath() {
		return merchantCertPath;
	}
	public void setMerchantCertPath(String merchantCertPath) {
		this.merchantCertPath = merchantCertPath;
	}
	public WechatAccount(String appID) {
		this.appID = appID;
	}
	public WechatAccount() {
	}
	public java.lang.String getAppSecret() {
		return appSecret;
	}
	
	@Id
	@GenericGenerator(name="gen1",strategy="assigned")
	@GeneratedValue(generator="gen1")
	public java.lang.String getAppID() {
		return appID;
	}

	public void setAppID(java.lang.String appID) {
		this.appID = appID;
	}
	
	/*public java.lang.String getUser_nickName() {
		return user_nickName;
	}

	public void setUser_nickName(java.lang.String user_nickName) {
		this.user_nickName = user_nickName;
	}*/
	
	@ManyToOne
	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setAppSecret(java.lang.String appSecret) {
		this.appSecret = appSecret;
	}
	
	public java.lang.String getToken() {
		return token;
	}
	public java.lang.String getAccountName() {
		return accountName;
	}
	public void setAccountName(java.lang.String accountName) {
		this.accountName = accountName;
	}
	public java.lang.String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(java.lang.String headImg) {
		this.headImg = headImg;
	}
	public void setToken(java.lang.String token) {
		this.token = token;
	}
	public java.lang.String getEncodingAESKey() {
		return encodingAESKey;
	}
	public void setEncodingAESKey(java.lang.String encodingAESKey) {
		this.encodingAESKey = encodingAESKey;
	}
	public Integer getAccountType() {
		return accountType;
	}
	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}
	@Override
	public void initializeAll() {
		domainInitializeAll(getUser());
	}
	
	/*public WechatConfig convert2WechatConfig(){
		WechatConfig wechatConfig = new WechatConfig();
		wechatConfig.setAppID(getAppID());
		wechatConfig.setAppSecret(getAppSecret());
		wechatConfig.setEncodingAESKey(getEncodingAESKey());
		wechatConfig.setToken(getToken());
		return wechatConfig;
	}*/

}
