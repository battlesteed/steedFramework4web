package steed.largewebsite.ogm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.ogm.OgmSession;
import org.hibernate.ogm.OgmSessionFactory;
import org.hibernate.ogm.boot.OgmSessionFactoryBuilder;
import org.hibernate.ogm.cfg.OgmProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import steed.domain.BaseDatabaseDomain;
import steed.domain.DomainScanner;
import steed.exception.runtime.BaseRuntimeException;
import steed.util.base.PathUtil;
import steed.util.base.PropertyUtil;
import steed.util.reflect.ReflectUtil;

/**
 * 获取线程安全的session
 * @author 战马
 */
public class OgmUtil {
	private static Logger logger = LoggerFactory.getLogger(OgmSessionFactory.class);
	private static ThreadLocal<OgmSession> threadLocal = new ThreadLocal<OgmSession>();
	private static OgmSessionFactory factory;
	private static ThreadLocal<Boolean> closeSession = new ThreadLocal<Boolean>();
	private static ThreadLocal<String> currentDatabase = new ThreadLocal<String>();
	private static ThreadLocal<Map<String, OgmSession>> sessionStory = new ThreadLocal<Map<String, OgmSession>>();
	private static Map<String, OgmSessionFactory> factoryMap = new HashMap<String, OgmSessionFactory>();
	public static final String mainFactory = "hibernate.properties";
	private static boolean isSignalMode = true;
	
	static {
		buildFactory();
		isSignalMode = PropertyUtil.getBoolean("isSignalNoSqlDatabase");
	}
	public static boolean getColseSession(){
		Boolean closeSession = OgmUtil.closeSession.get();
		if (closeSession == null) {
			return true;
		}
		return closeSession;
	}
	
	public static void setCloseSession(boolean closeSession){
		OgmUtil.closeSession.set(closeSession);
	}

	private static void buildFactory() {
		factory = buildFactory(mainFactory);
	}

	private static OgmSessionFactory buildFactory(String configFile) {
		try {
			
			Properties properties = getSettingProperties(configFile);
			
			StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
					.applySetting( OgmProperties.ENABLED, true )
					.applySettings(properties)
				    //assuming you are using JTA in a non container environment
//				    .applySetting( AvailableSettings.TRANSACTION_COORDINATOR_STRATEGY, "jta" )
//				    //assuming JBoss TransactionManager in standalone mode
//				    .applySetting( AvailableSettings.JTA_PLATFORM, "JBossTS" )
//				    //assuming Infinispan as the backend, using the default settings
//				    .applySetting( OgmProperties.DATASTORE_PROVIDER, Infinispan.DATASTORE_PROVIDER_NAME )
					.build();

			MetadataSources metadataSources = new MetadataSources( standardRegistry );
			
			DomainScanner instanceFromProperties = getDomainScanner();
			List<Class<? extends BaseDatabaseDomain>> scan = instanceFromProperties.scan(configFile);
			for(Class<? extends BaseDatabaseDomain> temp:scan){
				metadataSources.addAnnotatedClass(temp);
			}

			OgmSessionFactory sessionFactory = metadataSources.buildMetadata().getSessionFactoryBuilder().unwrap( OgmSessionFactoryBuilder.class ).build();

		
			factoryMap.put(configFile, sessionFactory);
			
			return sessionFactory;
			/*ProxoolConnectionProvider aConnectionProvider;*/
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("创建sessionFactory失败",e);
		}
		return null;
	}

	private static Properties getSettingProperties(String configFile) {
		Properties properties = new Properties();
		String classesPath = PathUtil.getClassesPath();
		String path = PathUtil.mergePath(classesPath, configFile);
		try {
			FileInputStream in = new FileInputStream(path);
			properties.load(in);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new BaseRuntimeException("文件"+path+"不存在",e);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

	public static DomainScanner getDomainScanner() {
		DomainScanner instanceFromProperties = ReflectUtil.getInstanceFromProperties("ogm.DomainScanner",PropertyUtil.configProperties);
		return instanceFromProperties;
	}

	public static OgmSession getSession(){
		OgmSession session = null;
		if (isSignalMode) {
			session = threadLocal.get();
		}else {
			session = getSessionMap().get(getCurrentDatabase());
		}
		if(session == null){
			session = getFactory().openSession();
			if (isSignalMode) {
				threadLocal.set(session);
			}else {
				getSessionMap().put(getCurrentDatabase(), session);
			}
		}
		return session;
	}
	
	private static Map<String, OgmSession> getSessionMap(){
		if (sessionStory.get() == null) {
			sessionStory.set(new HashMap<String, OgmSession>());
		}
		return sessionStory.get();
	}
	public static String getCurrentDatabase(){
		return currentDatabase.get()==null?mainFactory:currentDatabase.get();
	}
	
	public static OgmSessionFactory getFactory(){
		if (isSignalMode) {
			return factory;
		}else {
			OgmSessionFactory sessionFactory = factoryMap.get(getCurrentDatabase());
			if (sessionFactory == null) {
				return buildFactory(currentDatabase.get());
			}
			return sessionFactory;
		}
	}
	/**
	 * 切换数据库
	 * @param configFile
	 */
	public static void switchDatabase(String configFile){
		currentDatabase.set(configFile);
	} 
	
	public static void closeSession() {
		Session session = null;
		if (isSignalMode) {
			session = threadLocal.get();
		}else {
			session = getSessionMap().get(getCurrentDatabase());
		}
		if(session != null && session.isOpen()){
			session.close();
		}
		threadLocal.remove();
		closeSession.remove();
	}
	
	public static void release(){
		closeSession();
		for (Entry<String, OgmSession> e:getSessionMap().entrySet()) {
			Session value = e.getValue();
			if (value!= null&&value.isOpen()) {
				value.close();
			}
		}
		currentDatabase.remove();
		sessionStory.remove();
	}
}

