package steed.ext.domain.system;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import steed.domain.BaseRelationalDatabaseDomain;
import steed.domain.annotation.ValidateReg;
import steed.util.base.RegUtil;
@Entity
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Address extends BaseRelationalDatabaseDomain{
	private static final long serialVersionUID = 6175558172411441441L;
	@ValidateReg(reg={RegUtil.regChineseAndChar})
	private String province;
	@ValidateReg(reg={RegUtil.regChineseAndChar})
	private String city;
	@ValidateReg(reg={RegUtil.regChineseAndChar})
	private String area;
	private Integer id;
	private Integer fatherID;

	@Id
	@GenericGenerator(name="gen1",strategy="assigned")
	@GeneratedValue(generator="gen1")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Address(Integer address_id) {
		this.id = address_id;
	}

	public Address() {
	}
	public Address(String fullName) {
		setAddress(fullName);
	}
	public Address(Integer address_id,String fullName) {
		setAddress(fullName);
		this.id = address_id;
	}

	public void setAddress(String fullName) {
		String[] str = fullName.split("-");
		province = str[0];
		city = str[1];
		area = str[2];
	}

	@Override
	public String toString() {
		return (province + city + area).replaceAll("null", "");
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

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
	@Transient
	public Integer getFatherID() {
		if (fatherID != null) {
			return fatherID;
		}
		if (id == null) {
			return -1;
		}
		if (id % 10000 == 0) {
			fatherID = 0;
			return 0;
		}
		if (id % 100 == 0) {
			fatherID = id/10000*10000;
			return fatherID;
		}
		fatherID = id/100*100;
		return fatherID;
	}

	public void setFatherID(Integer fatherID) {
		this.fatherID = fatherID;
	}
	@Transient
	public boolean is_Province() {
		return this.id % 10000 == 0;
	}
	@Transient
	public boolean is_City() {
		return !is_Province() && !is_Area();
	}
	@Transient
	public boolean is_Area() {
		return this.id % 100 != 0;
	}

	@Override
	public void initializeAll() {
		this.initialize();
	}
}
