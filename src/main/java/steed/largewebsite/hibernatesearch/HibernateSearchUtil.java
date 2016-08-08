package steed.largewebsite.hibernatesearch;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Entity;

import org.hibernate.CacheMode;
import org.hibernate.ogm.OgmSession;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.annotations.Indexed;

import steed.largewebsite.ogm.DomainScanner;
import steed.largewebsite.ogm.OgmUtil;
import steed.largewebsite.ogm.domain.BaseNosqlDomain;
import steed.test.ogm.Dog;
import steed.util.base.PathUtil;
import steed.util.base.PropertyUtil;
import steed.util.file.FileUtil;

/**
 * hibernate search工具类
 * @author 战马
 * @email battle_steed@163.com
 *
 */
public class HibernateSearchUtil {
	
	/**
	 * 重新创建所有索引
	 */
	public static void rebuildWholeIndex(){
		OgmSession session = OgmUtil.getSession();
		FullTextSession fullTextSession = Search.getFullTextSession(session);
		try {
			DomainScanner domainScanner = OgmUtil.getDomainScanner();
			List<Class<? extends BaseNosqlDomain>> scan = domainScanner.scan(OgmUtil.getCurrentDatabase());
			Iterator<Class<? extends BaseNosqlDomain>> iterator = scan.iterator();
			while(iterator.hasNext()){
				Class<? extends BaseNosqlDomain> next = iterator.next();
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
