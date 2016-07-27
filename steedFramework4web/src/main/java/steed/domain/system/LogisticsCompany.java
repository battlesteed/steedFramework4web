package steed.domain.system;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import steed.domain.BaseDatabaseDomain;

@Entity
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class LogisticsCompany extends BaseDatabaseDomain{
	private static final long serialVersionUID = 2012029727907799622L;
	private String code;
	private String name;
	
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	@Id
	@GenericGenerator(name="gen1",strategy="assigned")
	@GeneratedValue(generator="gen1")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
