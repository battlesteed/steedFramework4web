package steed.domain.system;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Transient;

import org.hibernate.Hibernate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import steed.domain.BaseUnionKeyDomain;
import steed.domain.annotation.ValidateReg;
import steed.util.base.RegUtil;

@Entity
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@IdClass(steed.domain.system.PropertyID.class)
public class Property extends BaseUnionKeyDomain{
	private static final long serialVersionUID = 1L;
	@ValidateReg(reg={RegUtil.regChineseAndChar})
	private String kee;
	@ValidateReg(reg={RegUtil.regChineseAndChar})
	private String value;
	private Date createDate;
	
	private Date createDate_min_1;
	private Date createDate_max_1;
	/**
	 * 0,找回密码,1自动登录,2修改邮箱
	 */
	private String propertyType;
	@Id
	public String getPropertyType() {
		return propertyType;
	}
	@Transient
	public Date getCreateDate_min_1() {
		return createDate_min_1;
	}
	public void setCreateDate_min_1(Date createDate_min_1) {
		this.createDate_min_1 = createDate_min_1;
	}
	@Transient
	public Date getCreateDate_max_1() {
		return createDate_max_1;
	}
	public void setCreateDate_max_1(Date createDate_max_1) {
		this.createDate_max_1 = createDate_max_1;
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
