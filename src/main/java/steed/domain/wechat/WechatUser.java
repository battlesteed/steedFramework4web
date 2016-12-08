package steed.domain.wechat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import steed.hibernatemaster.domain.BaseRelationalDatabaseDomain;
import steed.util.base.StringUtil;
import steed.util.wechat.domain.result.WechatResult;

/**
 * 微信用户
 * @author 战马
 *
 */
@Entity
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class WechatUser extends BaseRelationalDatabaseDomain implements WechatResult{
	private static final long serialVersionUID = 1L;
	private String errcode;
	private String errmsg;
	private String openid;
	/**
	 * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知 
	 */
	private Integer sex;
	
	private String nickname;
	private String province;
	private String city;
	private String country;
	private String headimgurl;
	private String unionid;
	private String[] privilege;
	private String privilegeStr;
	private Integer subscribe;
	private Double integration;
	
	@Id
	@GenericGenerator(name="gen1",strategy="assigned")
	@GeneratedValue(generator="gen1")
	public String getOpenid() {
		return openid;
	}

	public Integer getSubscribe() {
		return subscribe;
	}

	public void setSubscribe(Integer subscribe) {
		this.subscribe = subscribe;
	}

	public Double getIntegration() {
		return integration;
	}

	public void setIntegration(Double integration) {
		this.integration = integration;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public Integer getSex() {
		return sex;
	}
	@Transient
	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}
	@Transient
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	@Transient
	public String[] getPrivilege() {
		return privilege;
	}

	public void setPrivilege(String[] privilege) {
		this.privilege = privilege;
		if (privilege == null) {
			privilegeStr = null;
		}else{
			if (privilege.length > 0) {
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < privilege.length; i++) {
					sb.append(privilege[i]).append(",");
				}
				privilegeStr = sb.substring(0, sb.length() - 1);
			}else {
				privilegeStr = "";
			}
		}
	}

	public String getPrivilegeStr() {
		return privilegeStr;
	}

	public void setPrivilegeStr(String privilegeStr) {
		this.privilegeStr = privilegeStr;
		if (privilegeStr == null) {
			privilege = null;
		}else {
			privilege = privilegeStr.split(",");
		}
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	@Override
	public void initializeAll() {
		initialize();
	}
	@Override
	public boolean save() {
		nickname = nickname.replace("💝", "?");
		return super.save();
	}

	@Override
	@Transient
	public boolean isSuccess() {
		return StringUtil.isStringEmpty(errcode);
	}
}
