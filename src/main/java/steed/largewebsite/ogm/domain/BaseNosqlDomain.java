package steed.largewebsite.ogm.domain;

import java.util.List;

import steed.hibernatemaster.domain.BaseDatabaseDomain;
import steed.largewebsite.ogm.NoSqlDao;
import steed.util.base.BaseUtil;
import steed.util.base.DomainUtil;

public abstract class BaseNosqlDomain extends BaseDatabaseDomain{
	private static final long serialVersionUID = -2920147124952496844L;

	@SuppressWarnings("unchecked")
	@Override
	public <T extends BaseDatabaseDomain> T smartGet(){
		return (T) NoSqlDao.smartGet(this);
	}
	
	@Override
	public boolean update(){
		return NoSqlDao.update(this);
	}
	
	@Override
	public boolean delete(){
		return NoSqlDao.delete(this);
	}
	
	@Override
	public boolean save(){
		return NoSqlDao.save(this);
	}
	
	@Override
	public boolean saveOrUpdate(){
		if (BaseUtil.isObjEmpty(DomainUtil.getDomainId(this))) {
			return save();
		}else {
			BaseDatabaseDomain smartGet = smartGet();
			if (smartGet != null) {
				NoSqlDao.evict(smartGet);
				return update();
			}else {
				return save();
			}
		}
	}
	
	@Override
	public boolean updateNotNullField(List<String> updateEvenNull){
		return updateNotNullField(updateEvenNull, false);
	}
	
	@Override
	public boolean updateNotNullField(List<String> updateEvenNull,boolean strictlyMode){
		return NoSqlDao.updateNotNullField(this, updateEvenNull,strictlyMode);
	}
}
