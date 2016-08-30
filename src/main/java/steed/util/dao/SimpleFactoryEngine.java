package steed.util.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import steed.domain.BaseDatabaseDomain;
import steed.domain.DomainScanner;
import steed.util.base.BaseUtil;

public class SimpleFactoryEngine implements FactoryEngine{
	
	public SessionFactory buildFactory(String configFile) {
		try {
			//configuration = new Configuration().configure(configFile);
//			StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
//			.applySettings(configuration.getProperties());
//			SessionFactory factory2 = configuration.buildSessionFactory(builder.build());
			
			
			StandardServiceRegistry standardRegistry = new   StandardServiceRegistryBuilder()
				    .configure(configFile)
				    .build();

			MetadataSources metadataSources = new MetadataSources( standardRegistry );
			
			DomainScanner instanceFromProperties = HibernateUtil.getDomainScanner();
			List<Class<? extends BaseDatabaseDomain>> scan = instanceFromProperties.scan(configFile);
			for(Class<? extends BaseDatabaseDomain> temp:scan){
				metadataSources.addAnnotatedClass(temp);
			}
			
		//	new HibernateUtil().scanDomain(metadataSources);
			
			Metadata metadata = metadataSources.getMetadataBuilder()
			    .applyImplicitNamingStrategy( ImplicitNamingStrategyJpaCompliantImpl.INSTANCE )
			    .build();

			SessionFactory factory2 = metadata.getSessionFactoryBuilder().build();
		
			
			return factory2;
			/*ProxoolConnectionProvider aConnectionProvider;*/
		} catch (Exception e) {
			e.printStackTrace();
			BaseUtil.getLogger().error("创建sessionFactory失败",e);
		}
		return null;
	}
}