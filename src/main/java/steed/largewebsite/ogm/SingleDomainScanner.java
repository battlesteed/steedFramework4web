package steed.largewebsite.ogm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import steed.largewebsite.ogm.domain.BaseNosqlDomain;
import steed.util.base.PathUtil;
import steed.util.base.PropertyUtil;
import steed.util.file.FileUtil;

/**
 * 单数据库实体类扫描器
 * @author 战马
 *
 */
public class SingleDomainScanner implements DomainScanner{
	
	public List<Class<? extends BaseNosqlDomain>> scan(String configFile){
		List<Class<? extends BaseNosqlDomain>> list = new ArrayList<>();
		if(!PropertyUtil.getBoolean("isSignalNoSqlDatabase")){
			return list;
		}
		String classesPath = PathUtil.getClassesPath();
		int len = classesPath.length() - 1;
		
		List<File> allFile = new FileUtil().getAllFile(classesPath,null);
		for (File f:allFile) {
			String absolutePath = f.getAbsolutePath();
			if (!absolutePath.endsWith(".class")) {
				continue;
			}
			String replaceAll = absolutePath.substring(len).replaceAll("\\\\", "/").replaceAll("\\/", ".");
			try {
				String domainClassName = replaceAll.substring(0,replaceAll.length() - 6);
				Class domainClass = Class.forName(domainClassName);
				if (BaseNosqlDomain.class.isAssignableFrom(domainClass)) {
					if (domainClass.getAnnotation(Entity.class) != null) {
						list.add(domainClass);
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
}
