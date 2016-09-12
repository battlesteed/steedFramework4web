package steed.ext.domain.system;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.Hibernate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import steed.domain.BaseRelationalDatabaseDomain;
import steed.domain.annotation.ValidateReg;
import steed.util.base.RegUtil;

@Entity
@Table(name="LoginLog")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class LoginLog extends BaseRelationalDatabaseDomain{
	private static final long serialVersionUID = 5725880859445051467L;
//	@ValidateReg(reg={RegUtil.CommonUseReg.chineseAndChar})
	private String nickName;
//	private People user;
	@ValidateReg(reg={RegUtil.regIpAddress})
	private String remoteAddr;
	private Date loginDate;
	private Date loginDate_max_1;
	private Date loginDate_min_1;
	private String message;
	@ValidateReg(reg={RegUtil.regChineseAndChar})
	private String address;
	private String area;
	private Integer loginResult;
	private Integer loginResult_not_equal_1;
	
	@Id
	@GenericGenerator(name="gen",strategy="assigned")
	@GeneratedValue(generator="gen")
	@Column(name="nickName")
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	@Column(name="remoteAddr")
	public String getRemoteAddr() {
		return remoteAddr;
	}
	@Transient
	public Integer getLoginResult_not_equal_1() {
		return loginResult_not_equal_1;
	}
	public void setLoginResult_not_equal_1(Integer loginResult_not_equal_1) {
		this.loginResult_not_equal_1 = loginResult_not_equal_1;
	}
	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}
	@Column(name="loginDate")
	public Date getLoginDate() {
		return loginDate;
	}
	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}
	@Column(name="message")
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Column(name="address")
	public String getAddress() {
		return address;
	}
	
	public Integer getLoginResult() {
		return loginResult;
	}
	public void setLoginResult(Integer loginResult) {
		this.loginResult = loginResult;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Column(name="area")
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	@Transient
	public Date getLoginDate_max_1() {
		return loginDate_max_1;
	}
	public void setLoginDate_max_1(Date loginDate_max_1) {
		this.loginDate_max_1 = loginDate_max_1;
	}
	@Transient
	public Date getLoginDate_min_1() {
		return loginDate_min_1;
	}
	public void setLoginDate_min_1(Date loginDate_min_1) {
		this.loginDate_min_1 = loginDate_min_1;
	}
	@Override
	public void initialize() {
		Hibernate.initialize(this);
	}
	@Override
	public void initializeAll() {
		initialize();
	}
}
