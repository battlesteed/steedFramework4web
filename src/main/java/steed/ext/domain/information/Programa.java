package steed.ext.domain.information;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import steed.hibernatemaster.domain.BaseRelationalDatabaseDomain;

/**
 * 栏目
 * @author 战马
 *
 */
@Entity
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Programa extends BaseRelationalDatabaseDomain{
	private static final long serialVersionUID = -2258584179191606674L;
	private String name;
	private String description;
	
	@Id
	@GenericGenerator(name="gen1",strategy="assigned")
	@GeneratedValue(generator="gen1")
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

	@Override
	public void initializeAll() {
		
	}

}
