package steed.largewebsite.hibernatesearch;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.hibernate.search.spi.BuildContext;
import org.hibernate.search.store.ShardIdentifierProviderTemplate;

public class DefaultShardIdentifierProvider extends ShardIdentifierProviderTemplate {

	 @Override
	 public String getShardIdentifier(Class<?> entityType, Serializable id,
	         String idAsString, Document document) {
	       addShard("steedDefauleShard");
	       return "steedDefauleShard";
//	    if (entityType.equals(Animal.class)) {
//	       String typeValue = document.getField("type").stringValue();
//	       addShard(typeValue);
//	       return typeValue;
//	    }
//	    throw new RuntimeException("Animal expected but found " + entityType);
	 }

	 @Override
	 protected Set<String> loadInitialShardNames(Properties properties, BuildContext buildContext) {
		  return new HashSet<String>(Arrays.asList("steedDefauleShard"));
	 /*   ServiceManager serviceManager = buildContext.getServiceManager();
	    SessionFactory sessionFactory = serviceManager.requestService(
	        HibernateSessionFactoryService.class).getSessionFactory();
	    Session session = sessionFactory.openSession();
	    try {
	       Criteria initialShardsCriteria = session.createCriteria(Animal.class);
	       initialShardsCriteria.setProjection(Projections.distinct(Property.forName("type")));
	       List<String> initialTypes = initialShardsCriteria.list();
	       return new HashSet<String>(initialTypes);
	    }finally {
	       session.close();
	    }*/
	 }
	 
	}
