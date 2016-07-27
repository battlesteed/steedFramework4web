package steed.domain;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Hibernate;

import steed.util.base.BaseUtil;
import steed.util.base.DomainUtil;
import steed.util.dao.DaoUtil;
public abstract class BaseDatabaseDomain extends BaseDomain{
	private static final long serialVersionUID = -6872283825535497093L;

	public void initialize(){
		Hibernate.initialize(this);
	}
	/**
	 * 不为null就initialize,避免子类getXXX().initializeAll()时
	 * 先要判断getXXX()是否为null
	 */
	protected void domainInitializeAll(BaseDatabaseDomain domain){
		if (domain != null) {
			domain.initializeAll();
		}
	}
	/**
	 * initialize set中的domain
	 */
	protected void domainInitializeSetAll(Set<? extends BaseDatabaseDomain> set){
		if (set != null) {
			for(BaseDatabaseDomain temp:set){
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
	
	public void initDefaultValue(){
		Map<Class, Object> map = new HashMap<Class, Object>();
		map.put(Integer.class, 0);
		map.put(Long.class, 0L);
		map.put(Date.class, new Date());
		initDefaultValue(map);
	}
	public void initDefaultValue(Map<Class, Object> map){
		for (Field f:this.getClass().getDeclaredFields()) {
			Object value = map.get(f.getType());
			if (value != null) {
				f.setAccessible(true);
				try {
					if (BaseUtil.isObjEmpty(f.get(this))) {
						f.set(this, value);
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
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
	@SuppressWarnings("unchecked")
	public <T extends BaseDatabaseDomain> T smartGet(){
		return (T) DaoUtil.smartGet(this);
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
	@SuppressWarnings("unchecked")
	public <T> List<T> listAll(){
		return (List<T>) DaoUtil.listAllObj(this);
	}
	public boolean updateNotNullField(List<String> updateEvenNull){
		return updateNotNullField(updateEvenNull, false);
	}
	
	public boolean updateNotNullField(List<String> updateEvenNull,boolean strictlyMode){
		return DaoUtil.updateNotNullField(this, updateEvenNull,strictlyMode);
	}
	
	
	@Override
	public String toString() {
		Serializable id = DomainUtil.getDomainId(this);
		if (id == null) {
			return "";
		}
		return String.valueOf(id);
	}
	
}
