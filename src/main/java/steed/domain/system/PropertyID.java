package steed.domain.system;

import steed.domain.DomainID;
import steed.domain.annotation.ValidateReg;
import steed.util.base.RegUtil;

public class PropertyID extends DomainID{
	private static final long serialVersionUID = 1L;
	@ValidateReg(reg={RegUtil.regChineseAndChar})
	private String kee;
	private String propertyType;	
	public PropertyID() {
	}
	public PropertyID(String kee, String propertyType) {
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
	public String getPropertyType() {
		return propertyType;
	}
	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}
}
