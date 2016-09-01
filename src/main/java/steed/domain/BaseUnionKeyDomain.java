package steed.domain;

import java.io.Serializable;
import java.lang.reflect.Field;

import javax.persistence.Id;
import javax.persistence.IdClass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import steed.domain.annotation.NotQueryCondition;
import steed.util.base.ExceptionUtil;
import steed.util.reflect.ReflectUtil;

/**
 * 联合主键基础类，若不想实现UnionKeyDomain可继承该类
 * @author 战马
 */
public abstract class BaseUnionKeyDomain extends BaseDatabaseDomain implements UnionKeyDomain{
	private static final long serialVersionUID = 7359662643996245180L;
	@NotQueryCondition
	private static Logger logger = LoggerFactory.getLogger(BaseUnionKeyDomain.class);
	
	@Override
	public void setDomainID(Serializable unionKeyDomain) {
		Class<? extends BaseUnionKeyDomain> domainClass = this.getClass();
		for (Field f:ReflectUtil.getNotFinalFiles(unionKeyDomain)) {
			f.setAccessible(true);
			try {
				Field declaredField = domainClass.getDeclaredField(f.getName());
				declaredField.setAccessible(true);
				try {
					declaredField.set(this, f.get(unionKeyDomain));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
				StringBuffer sb = new StringBuffer(domainClass.getName());
				sb.append("中没有与");
				sb.append(unionKeyDomain.getClass().getName());
				sb.append("对应的");
				sb.append(f.getName());
				sb.append("字段!!!");
				ExceptionUtil.throwRuntimeException(sb, logger);
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
	}


	@Override
	@Id
	public DomainID getDomainID(){
		Class<? extends BaseUnionKeyDomain> thisClass = this.getClass();
		String name = thisClass.getName();
		IdClass idClass = thisClass.getAnnotation(IdClass.class);
		if (idClass == null) {
			StringBuffer sb = new StringBuffer("在");
			sb.append(name);
			sb.append("中没有javax.persistence.IdClass注解!!!");
			ExceptionUtil.throwRuntimeException(sb, logger);
		}
		@SuppressWarnings("unchecked")
		Class<? extends DomainID> domainIDClass = idClass.value();
		try {
			DomainID domainID = domainIDClass.newInstance();
			for(Field f:ReflectUtil.getNotFinalFiles(domainIDClass)){
				try {
					Field thisField = thisClass.getDeclaredField(f.getName());
					thisField.setAccessible(true);
					f.setAccessible(true);
					f.set(domainID, thisField.get(this));
				} catch (NoSuchFieldException e) {
					ExceptionUtil.throwRuntimeException(String.format("%s中没有与%s对应的%s字段!!!", name,domainIDClass.getName(),f.getName()), logger);
				}
			}
			return domainID;
		} catch (InstantiationException e) {
			StringBuffer sb = new StringBuffer(domainIDClass.getName());
			sb.append("是抽象的或没有空构造函数,导致无法实例化!!!");
			ExceptionUtil.throwRuntimeException(sb, logger);
		} catch (IllegalAccessException e) {
			StringBuffer sb = new StringBuffer(domainIDClass.getName());
			sb.append("没有public的空构造函数!!!");
			ExceptionUtil.throwRuntimeException(sb, logger);
		}
		return null;
	}
}
