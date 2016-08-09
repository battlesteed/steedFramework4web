package steed.domain;

import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;

import steed.util.base.BaseUtil;
import steed.util.base.DomainUtil;
import steed.util.dao.DaoUtil;

public class BaseRelationalDatabaseDomain extends BaseDatabaseDomain{
	public void initialize(){
		Hibernate.initialize(this);
	}
	/**
	 * 不为null就initialize,避免子类getXXX().initializeAll()时
	 * 先要判断getXXX()是否为null
	 */
	protected void domainInitializeAll(BaseRelationalDatabaseDomain domain){
		if (domain != null) {
			domain.initializeAll();
		}
	}
	/**
	 * initialize set中的domain
	 */
	protected void domainInitializeSetAll(Set<? extends BaseRelationalDatabaseDomain> set){
		if (set != null) {
			for(BaseRelationalDatabaseDomain temp:set){
				temp.initializeAll();
			}
		}
	}
	
	/*
	 * initializeAll的深度，如过为零，则只initialize本身。
	 * @param deep
	 *public void initializeAll(int deep){
		if (deep > 0) {
			
		}else if (deep == 0) {
			this.initialize();
		}
		
	}*/
	public void initializeAll(){
		initialize();
	}
	public <T extends BaseDatabaseDomain> T smartGet(){
		return (T) DaoUtil.smartGet(this);
	}
	
	public boolean update(){
		return DaoUtil.update(this);
	}
	public boolean delete(){
		return DaoUtil.delete(this);
	}
	public boolean save(){
		return DaoUtil.save(this);
	}
	
	public boolean saveOrUpdate(){
		if (BaseUtil.isObjEmpty(DomainUtil.getDomainId(this))) {
			return save();
		}else {
			BaseDatabaseDomain smartGet = smartGet();
			if (smartGet != null) {
				DaoUtil.evict(smartGet);
				return update();
			}else {
				return save();
			}
		}
	}
	
	public boolean updateNotNullField(List<String> updateEvenNull){
		return updateNotNullField(updateEvenNull, false);
	}
	
	public boolean updateNotNullField(List<String> updateEvenNull,boolean strictlyMode){
		return DaoUtil.updateNotNullField(this, updateEvenNull,strictlyMode);
	}
	/*public void initializeAll(){
		this.initialize();
		Field[] fields = this.getClass().getDeclaredFields();
		for(Field f:fields){
			try {
				f.setAccessible(true);
				Object obj = f.get(this);
				if (BaseUtil.isObjEmpty(obj)) {
					continue;
				}
				if (obj instanceof BaseDatabaseDomain) {
					((BaseDatabaseDomain) obj).initializeAll();
				}else if (CollectionsUtil.isObjCollections(obj)) {
					Object[] objects = CollectionsUtil.collections2Array(obj);
					if (objects == null || objects.length < 1) {
						continue;
					}
					if (!(objects[0] instanceof BaseDatabaseDomain)) {
						continue;
					}
					for (BaseDatabaseDomain o:(BaseDatabaseDomain[])objects) {
						if (BaseUtil.isObjEmpty(o)) {
							continue;
						}
						o.initializeAll();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}*/
	
	/*	public boolean validate(){
		Class<? extends BaseDatabaseDomain> clazz = this.getClass();
		Properties IDReg = PropertyUtil.getProperties("IDReg.properties");
		String reg = IDReg.getProperty(clazz.getName());
		if (BaseUtil.isStringEmpty(reg)) {
			return true;
		}
		Serializable domainId = DomainUtil.getDomainId(this);
		if (BaseUtil.isObjEmpty(domainId)) {
			return true;
		}
		Pattern p = Pattern.compile(reg);
		return p.matcher(domainId.toString()).find();
	}*/
	@SuppressWarnings("unchecked")
	public <T> List<T> listAll(){
		return (List<T>) DaoUtil.listAllObj(this);
	}
}
