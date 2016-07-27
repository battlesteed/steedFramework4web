package steed.domain;

import java.beans.Transient;
import java.io.Serializable;

import steed.util.base.DomainUtil;

public abstract class BaseDomain implements Serializable{
	private static final long serialVersionUID = 3930474378471185938L;
	private Object tempData;
//	private static Logger logger = Logger.getLogger(BaseDomain.class);
	/* public boolean validate(){
		 return true;
	 }*/
	/*public boolean validate(){
		Class<? extends BaseDomain> clazz = this.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field f:fields) {
			try {
				f.setAccessible(true);
				Object obj = f.get(this);
				if (BaseUtil.isObjEmpty(obj)) {
					continue;
				}
				if(obj instanceof String){
					if (f.getAnnotation(AllowSpecialCharacter.class) == null) {
						obj = ((String)obj).replaceAll("'", "＇");
						obj = StringUtil.transferrCharacter((String)obj);
					}
					f.set(this, obj);
				}
				
				ValidateReg validateReg = f.getAnnotation(ValidateReg.class);
				if (validateReg == null) {
					continue;
				}
				String[] regs = validateReg.reg();
				for(String str:regs){
					Pattern p = Pattern.compile(str);
					boolean find = p.matcher(obj + "").find();
					if (!find) {
						return false;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}*/
	/*public boolean validateALL(){
		return true;
	}*/
	/*public boolean validateALL(){
		Class<? extends BaseDomain> clazz = this.getClass();
		Field[] fields = clazz.getDeclaredFields();
		boolean returned = true;
		for (Field f:fields) {
			f.setAccessible(true);
			Object object;
			try {
				object = f.get(this);
				if (BaseUtil.isObjEmpty(object)) {
					continue;
				}
				if (object instanceof BaseDomain) {
					returned = ((BaseDomain)object).validateALL();
				}
				if (!returned) {
					return false;
				}
				
				*/
	
				/*
				if (CollectionsUtil.isObjCollections(object)) {
					if (object instanceof BaseDatabaseDomain) {
						for (Object o:(Collection)object) {
							if (o instanceof BaseDomain) {
								returned = ((BaseDomain) o).validateALL();
								if (!returned) {
									return false;
								}
							}
						}
					}else if (object instanceof Map) {
						Map map = (Map)object;
						for (Object o:map.keySet()) {
							Object temp = map.get(o);
							if (temp instanceof BaseDomain) {
								returned = ((BaseDomain) temp).validateALL();
								if (!returned) {
									return false;
								}
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return this.validate();
	}*/
	
	public void setVirtualID(Serializable serializable){
		DomainUtil.setDomainId(this,serializable);
	}
	@javax.persistence.Transient
	public Serializable getVirtualID(){
		return DomainUtil.getDomainId(this);
	}
	@Transient
	public Object getTempData() {
		return tempData;
	}

	public void setTempData(Object tempData) {
		this.tempData = tempData;
	}

	@Override
	public int hashCode() {
		return DomainUtil.domainHashCode(this);
	}
	@Override
	public boolean equals(Object obj) {
		return DomainUtil.domainEquals(this, obj);
	}
}
