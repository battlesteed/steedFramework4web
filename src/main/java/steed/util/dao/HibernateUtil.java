package steed.util.dao;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Entity;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import steed.util.base.PathUtil;
import steed.util.base.PropertyUtil;
import steed.util.file.FileUtil;
/**
 * 获取线程安全的session
 * @author 战马
 */
public class HibernateUtil{
	private static Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
	private static ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
	private static SessionFactory factory;
	private static ThreadLocal<Boolean> closeSession = new ThreadLocal<Boolean>();
	private static ThreadLocal<String> currentDatabase = new ThreadLocal<String>();
	private static ThreadLocal<Map<String, Session>> sessionStory = new ThreadLocal<Map<String, Session>>();
//	private static boolean whole_closeSession = false;
	private static Map<String, SessionFactory> factoryMap = new HashMap<String, SessionFactory>();
	public static final String mainFactory = "hibernate.cfg.xml";
	private static boolean isSignalMode = true;
	
	static {
		buildFactory();
		isSignalMode = PropertyUtil.getBoolean("isSignalDatabase");
	}
	public static boolean getColseSession(){
		Boolean closeSession = HibernateUtil.closeSession.get();
		if (closeSession == null) {
			return true;
		}
		return closeSession;
	}
	
	public static void setCloseSession(boolean closeSession){
		HibernateUtil.closeSession.set(closeSession);
	}

	private static void buildFactory() {
		factory = buildFactory(mainFactory);
	}

	private static SessionFactory buildFactory(String configFile) {
		try {
			//configuration = new Configuration().configure(configFile);
//			StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
//			.applySettings(configuration.getProperties());
//			SessionFactory factory2 = configuration.buildSessionFactory(builder.build());
			
			
			StandardServiceRegistry standardRegistry = new   StandardServiceRegistryBuilder()
				    .configure(configFile)
				    .build();

			MetadataSources metadataSources = new MetadataSources( standardRegistry );
			new HibernateUtil().scanDomain(metadataSources);
			
			Metadata metadata = metadataSources.getMetadataBuilder()
			    .applyImplicitNamingStrategy( ImplicitNamingStrategyJpaCompliantImpl.INSTANCE )
			    .build();

			SessionFactory factory2 = metadata.getSessionFactoryBuilder().build();
		
			factoryMap.put(configFile, factory2);
			
			return factory2;
			/*ProxoolConnectionProvider aConnectionProvider;*/
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("创建sessionFactory失败",e);
		}
		return null;
	}
	
	private void scanDomain(MetadataSources source){

		String classesPath = PathUtil.getClassesPath();
		int len = classesPath.length() - 1;
		
		List<File> allFile = new FileUtil().getAllFile(classesPath,null);
		for (File f:allFile) {
			String absolutePath = f.getAbsolutePath();
			if(absolutePath.contains("domain") && PropertyUtil.getBoolean("isSignalDatabase")){
				String replaceAll = absolutePath.substring(len).replaceAll("\\\\", "/").replaceAll("\\/", ".");
				try {
					String domainClassName = replaceAll.substring(0,replaceAll.length() - 6);
					Class domainClass = Class.forName(domainClassName);
					Entity entity = (Entity) domainClass.getAnnotation(Entity.class);
					if (entity != null) {
						source.addAnnotatedClass(domainClass);
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static Session getSession(){
		Session session = null;
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
	
	private static Map<String, Session> getSessionMap(){
		if (sessionStory.get() == null) {
			sessionStory.set(new HashMap<String, Session>());
		}
		return sessionStory.get();
	}
	private static String getCurrentDatabase(){
		return currentDatabase.get()==null?mainFactory:currentDatabase.get();
	}
	
	public static SessionFactory getFactory(){
		if (isSignalMode) {
			return factory;
		}else {
			SessionFactory sessionFactory = factoryMap.get(getCurrentDatabase());
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
	
	/*public static void change2SqlServer(){
		threadLocal.set(HibernateUtil4Sqlserver.getSession());
	}
	public static void change2MySql(){
		threadLocal.set(getSession());
	}*/
	
	/*public static SessionFactory getFactory(){
		if (factory == null) {
			builtFactory();
		}
		return factory;
	}*/
	
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
		for (Entry<String, Session> e:getSessionMap().entrySet()) {
			Session value = e.getValue();
			if (value!= null&&value.isOpen()) {
				value.close();
			}
		}
		currentDatabase.remove();
		sessionStory.remove();
	}
/*	public static boolean isWhole_closeSession() {
		return whole_closeSession;
	}*/
	
	/**
	 * 设置是否统一关session,如果是,则getColseSession()默认返回false否则默认返回true
	 * @param whole_closeSession
	 */
	/*public static void setWhole_closeSession(boolean whole_closeSession) {
		HibernateUtil.whole_closeSession = whole_closeSession;
	}*/
}
