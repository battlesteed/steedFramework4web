package steed.util.dao;

import org.hibernate.SessionFactory;

public interface FactoryEngine {
	SessionFactory buildFactory(String configFile) ;
}