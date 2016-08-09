package steed.test.ogm;

import java.util.List;

import org.hibernate.CacheMode;
import org.hibernate.Transaction;
import org.hibernate.ogm.OgmSession;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.batchindexing.MassIndexerProgressMonitor;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.junit.Test;

import steed.largewebsite.hibernatesearch.HibernateSearchUtil;
import steed.largewebsite.ogm.OgmUtil;
import steed.util.base.BaseUtil;

public class TestMongoDBSession {
	@Test
	public void startUp(){
		Dog dina = new Dog();
		dina.setId(132L);
		dina.setName("战马");
		dina.setName2("battlesteed");
		dina.update();
	}
	@Test
	public void testHibernateSearch(){
		OgmSession session = OgmUtil.getSession();
		FullTextSession fullTextSession = Search.getFullTextSession(session);
		HibernateSearchUtil.rebuildWholeIndex();

		Transaction tx = fullTextSession.beginTransaction();
		// create native Lucene query using the query DSL
		// alternatively you can write the Lucene query using the Lucene query parser
		// or the Lucene programmatic API. The Hibernate Search DSL is recommended though
		QueryBuilder qb = fullTextSession.getSearchFactory()
		  .buildQueryBuilder().forEntity(Dog.class).get();
		org.apache.lucene.search.Query query = qb
		  .keyword()
		  .onFields("name2")
		  .matching("Diddndddda")
		  .createQuery();

		// wrap Lucene query in a org.hibernate.Query
		org.hibernate.Query hibQuery =
		    fullTextSession.createFullTextQuery(query, Dog.class);

		// execute search
		List result = hibQuery.list();
		BaseUtil.outJson(result.size());
		
		tx.commit();
		session.close();
	}
}
