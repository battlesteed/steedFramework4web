package steed.domain.system;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import steed.domain.annotation.ValidateReg;
import steed.exception.runtime.system.FrameworkException;
import steed.hibernatemaster.domain.BaseRelationalDatabaseDomain;
import steed.util.base.RegUtil;

@Entity
@Table(name="Config")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Config extends BaseRelationalDatabaseDomain{
	private static final long serialVersionUID = 1L;
	@ValidateReg(reg={RegUtil.regChineseAndChar})
	private String kee;
	@ValidateReg(reg={RegUtil.regChineseAndChar})
	private String value;
	
	public Config() {
	}
	public Config(String kee, String value) {
		this.kee = kee;
		this.value = value;
	}
	
	public Config(String kee) {
		this.kee = kee;
	}
	@Id
	@GenericGenerator(name="generator",strategy="assigned")
	@GeneratedValue(generator="generator")
	@Column(name="kee")
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
	@Transient
	public Date getDate(){
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return simpleDateFormat.parse(getValue());
		} catch (ParseException e) {
			SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
			try {
				return simpleDateFormat2.parse(getValue());
			} catch (ParseException e1) {
				e1.printStackTrace();
				throw new FrameworkException(getValue()+"不符合yyyy-MM-dd HH:mm:ss或yyyy-MM-dd日期格式");
			}
			
		}
	}
	public void setDate(Date date){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		setValue(simpleDateFormat.format(date));
	}
	
	
	@Transient
	public Long getLongValue(){
		return Long.parseLong(getValue());
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
