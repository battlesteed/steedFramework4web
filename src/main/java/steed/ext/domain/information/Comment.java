package steed.ext.domain.information;

import steed.hibernatemaster.domain.BaseRelationalDatabaseDomain;

public class Comment extends BaseRelationalDatabaseDomain{
	private static final long serialVersionUID = -7357953475167253685L;

	@Override
	public void initializeAll() {
		this.initialize();
		
	}

}
