package steed.ext.domain.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.Hibernate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import steed.domain.BaseRelationalDatabaseDomain;
import steed.domain.annotation.ValidateReg;
import steed.util.base.RegUtil;

@Entity
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Power extends BaseRelationalDatabaseDomain{
	private static final long serialVersionUID = 8841822962310540390L;
	@ValidateReg(reg={RegUtil.CommonUsedReg.chineseAndChar})
	private String name;
	private String description;
	public Power() {
	}
	public Power(String name) {
		this.name = name;
	}
	@Id
	@GenericGenerator(name="generator",strategy="assigned")
	@GeneratedValue(generator="generator")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	/*@Override
	public boolean equals(Object obj) {
		if (obj instanceof Power){
			String name2 = ((Power) obj).getName();
			if (name != null) {
				return name.equals(name2);
			}
			return name2 == null;
		}else{
			return false;
		}
	}*/
	/*@Override
	public int hashCode() {
		if (name == null) {
			return super.hashCode();
		}
		return this.name.hashCode();
	}*/
	@Override
	public void initialize() {
		Hibernate.initialize(this);
	}
	@Override
	public void initializeAll() {
		initialize();
	}
}
