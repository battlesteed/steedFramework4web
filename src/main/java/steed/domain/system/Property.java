package steed.domain.system;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import org.hibernate.Hibernate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import steed.domain.BaseUnionKeyDomain;
import steed.domain.annotation.ValidateReg;
import steed.util.base.RegUtil;
import steed.util.base.StringUtil;

@Entity
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@IdClass(steed.domain.system.PropertyID.class)
public class Property extends BaseUnionKeyDomain{
	private static final long serialVersionUID = 1L;
	@ValidateReg(reg={RegUtil.CommonUsedReg.chineseAndChar})
	private String kee;
	@ValidateReg(reg={RegUtil.CommonUsedReg.chineseAndChar})
	private String value;
	private Date createDate;
	/**
	 * 0,找回密码,1自动登录,2修改邮箱
	 */
	private String propertyType;
	@Id
	public String getPropertyType() {
		return propertyType;
	}
	public Property(String kee, String value, String propertyType) {
		super();
		this.kee = kee;
		this.value = value;
		this.propertyType = propertyType;
	}
	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}
	public Property() {
	}
	public Property(String kee, String value) {
		this.kee = kee;
		this.value = value;
	}
	
	public Property(String kee) {
		this.kee = kee;
	}
	@Id
	public String getKee() {
		return kee;
	}
	public void setKee(String kee) {
		this.kee = kee;
	}
	@Column(name="value")
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public void initialize() {
		Hibernate.initialize(this);
	}
	
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * 初始化
	 */
	public void init(){
		createDate = new Date();
		kee = StringUtil.getSecureRandomString();
	}
	
	@Override
	public boolean save() {
		createDate = new Date();
		return super.save();
	}
	@Override
	public void initializeAll() {
		initialize();
	}
	
}