package steed.util.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import steed.domain.BaseDatabaseDomain;
import steed.domain.DomainScanner;
import steed.util.base.PropertyUtil;
import steed.util.reflect.ReflectUtil;
/**
 * 获取线程安全的session<br>
                   _ooOoo_
                  o8888888o
                  88" . "88
                  (| -_- |)
                  O\  =  /O
               ____/`---'\____
             .'  \\|     |//  `.
            /  \\|||  :  |||//  \
           /  _||||| -:- |||||-  \
           |   | \\\  -  /// |   |
           | \_|  ''\---/''  |   |
           \  .-\__  `-`  ___/-. /
         ___`. .'  /--.--\  `. . __
      ."" '<  `.___\_<|>_/___.'  >'"".
     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
     \  \ `-.   \_ __\ /__ _/   .-` /  /
======`-.____`-.___\_____/___.-`____.-'======
                   `=---='
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
         佛祖保佑       永无BUG 
   佛曰:
//                  写字楼里写字间，写字间里程序员；
//                  程序人员写程序，又拿程序换酒钱。
//                  酒醒只在网上坐，酒醉还来网下眠；
//                  酒醉酒醒日复日，网上网下年复年。
//                  但愿老死电脑间，不愿鞠躬老板前；
//                  奔驰宝马贵者趣，公交自行程序员。
//                  别人笑我忒疯癫，我笑自己命太贱；
//                  不见满街漂亮妹，哪个归得程序员？
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
	private static FactoryEngine factoryEngine = ReflectUtil.getInstanceFromProperties("dao.factoryEngine", PropertyUtil.configProperties);
	
	
	static {
		try{
			buildFactory();
			isSignalMode = PropertyUtil.getBoolean("isSignalDatabase");
		}catch (Error e) {
			e.printStackTrace();
			throw e;
		}
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
		SessionFactory buildFactory = factoryEngine.buildFactory(configFile);
		factoryMap.put(configFile, buildFactory);
		return buildFactory;
	}
	
	public static DomainScanner getDomainScanner() {
		DomainScanner instanceFromProperties = ReflectUtil.getInstanceFromProperties("dao.DomainScanner",PropertyUtil.configProperties);
		return instanceFromProperties;
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
