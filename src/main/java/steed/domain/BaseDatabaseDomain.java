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
	
	public abstract boolean update();
	public abstract boolean delete();
	public abstract boolean save();
	
	public abstract <T extends BaseDatabaseDomain> T smartGet();
	public abstract boolean saveOrUpdate();

	public abstract boolean updateNotNullField(List<String> updateEvenNull);
	
	public abstract boolean updateNotNullField(List<String> updateEvenNull,boolean strictlyMode);
	
	
	@Override
	public String toString() {
		Serializable id = DomainUtil.getDomainId(this);
		if (id == null) {
			return "";
		}
		return String.valueOf(id);
	}
	
}
