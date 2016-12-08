package steed.largewebsite.hibernatesearch;

import java.util.Iterator;
import java.util.List;

import org.hibernate.CacheMode;
import org.hibernate.ogm.OgmSession;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.annotations.Indexed;

import steed.exception.PathIsTopException;
import steed.hibernatemaster.domain.BaseDatabaseDomain;
import steed.hibernatemaster.domain.DomainScanner;
import steed.largewebsite.ogm.OgmUtil;
import steed.util.base.PathUtil;

/**
 * hibernate search工具类
 * @author 战马
 * @email battle_steed@163.com
 *
 */
public class HibernateSearchUtil {
	
	static{
		try {
			System.setProperty("hibernatesearch.infinispanDataDir", PathUtil.mergePath(PathUtil.getParaentPath(PathUtil.getClassesPath()), "infinispan"));
		} catch (PathIsTopException e) {
			e.printStackTrace();
		}
	}
	
	public static FullTextSession getSession(){
		return Search.getFullTextSession(OgmUtil.getSession());
	}
	
	/**
	 * 重新创建所有索引
	 */
	public static void rebuildWholeIndex(){
		OgmSession session = OgmUtil.getSession();
		FullTextSession fullTextSession = Search.getFullTextSession(session);
		try {
			DomainScanner domainScanner = OgmUtil.getDomainScanner();
			List<Class<? extends BaseDatabaseDomain>> scan = domainScanner.scan(OgmUtil.getCurrentDatabase());
			Iterator<Class<? extends BaseDatabaseDomain>> iterator = scan.iterator();
			while(iterator.hasNext()){
				Class<? extends BaseDatabaseDomain> next = iterator.next();
				if (next.getAnnotation(Indexed.class) == null) {
					iterator.remove();
				}
			}
			
			fullTextSession
			 .createIndexer(scan.toArray(new Class<?>[0]))
			 .batchSizeToLoadObjects( 25 )
			 .cacheMode( CacheMode.NORMAL )
			 .threadsToLoadObjects( 12 )
			 .idFetchSize( 150 )
			 .transactionTimeout( 1800 ).startAndWait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
}
