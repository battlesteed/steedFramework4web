package steed.ext.domain.system;

import steed.domain.DomainID;
import steed.domain.annotation.ValidateReg;
import steed.util.base.RegUtil;

public class PropertyID extends DomainID{
	private static final long serialVersionUID = 1L;
	@ValidateReg(reg={RegUtil.CommonUsedReg.chineseAndChar})
	private String kee;
	private Integer propertyType;	
	public PropertyID() {
	}
	public PropertyID(String kee, Integer propertyType) {
		super();
		this.kee = kee;
		this.propertyType = propertyType;
	}
	public String getKee() {
		return kee;
	}
	public void setKee(String kee) {
		this.kee = kee;
	}
	public Integer getPropertyType() {
		return propertyType;
	}
	public void setPropertyType(Integer propertyType) {
		this.propertyType = propertyType;
	}
}
