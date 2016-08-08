package steed.largewebsite.ogm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import steed.largewebsite.ogm.domain.BaseNosqlDomain;
import steed.util.base.PathUtil;
import steed.util.base.PropertyUtil;
import steed.util.file.FileUtil;

public interface DomainScanner {
	
	public List<Class<? extends BaseNosqlDomain>> scan(String configFile);
}
