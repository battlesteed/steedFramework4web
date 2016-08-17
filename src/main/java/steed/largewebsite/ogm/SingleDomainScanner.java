package steed.largewebsite.ogm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import steed.domain.BaseDatabaseDomain;
import steed.domain.DomainScanner;
import steed.largewebsite.ogm.domain.BaseNosqlDomain;
import steed.util.base.BaseUtil;
import steed.util.base.PathUtil;
import steed.util.base.PropertyUtil;
import steed.util.file.FileUtil;

/**
 * 单数据库实体类扫描器
 * @author 战马
 *
 */
public class SingleDomainScanner implements DomainScanner{
	
	public List<Class<? extends BaseDatabaseDomain>> scan(String configFile){
		List<Class<? extends BaseDatabaseDomain>> list = new ArrayList<>();
		if(!PropertyUtil.getBoolean("isSignalNoSqlDatabase")){
			return list;
		}
		String classesPath = PathUtil.getClassesPath();
		int len = classesPath.length() - 1;
		
		List<File> allFile = new FileUtil().getAllFile(classesPath,null);
		if (PropertyUtil.getBoolean("devMode")) {
			allFile.addAll(new FileUtil().getAllFile(classesPath.replaceFirst("classes", "test-classes"),null));
			allFile.addAll(new FileUtil().getAllFile(classesPath.replaceFirst("test-classes", "classes"),null));
		}
		
		for (File f:allFile) {
			String absolutePath = f.getAbsolutePath();
			if (!absolutePath.endsWith(".class") || (!absolutePath.contains("domain") && !absolutePath.contains("model"))) {
				continue;
			}
//			String replaceAll = absolutePath.substring(len).replaceAll("\\\\", "/").replaceAll("\\/", ".");
			String replaceAll = absolutePath.substring(absolutePath.indexOf("classes")+"classes.".length()).replaceAll("\\\\", "/").replaceAll("\\/", ".");
			
			try {
				String domainClassName = replaceAll.substring(0,replaceAll.length() - 6);
				BaseUtil.out("扫描",domainClassName);
				
				@SuppressWarnings("unchecked")
				Class<? extends BaseDatabaseDomain> domainClass = (Class<? extends BaseDatabaseDomain>) Class.forName(domainClassName);
				if (BaseNosqlDomain.class.isAssignableFrom(domainClass)) {
					if (domainClass.getAnnotation(Entity.class) != null) {
						list.add(domainClass);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}catch (Error e) {
				e.printStackTrace();
			}
		}
		return list;
	}
}
