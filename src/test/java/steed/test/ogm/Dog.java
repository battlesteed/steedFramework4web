/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package steed.test.ogm;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import steed.largewebsite.ogm.domain.BaseNosqlDomain;

/**
 * @author Emmanuel Bernard
 */
@Indexed
@Entity
public class Dog extends BaseNosqlDomain{
	
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.YES)
	private String name2;


	public String getName2() {
		return name2;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}
	private Long id;
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Id
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
