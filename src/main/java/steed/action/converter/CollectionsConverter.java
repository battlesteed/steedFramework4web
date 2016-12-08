package steed.action.converter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.struts2.util.StrutsTypeConverter;

import com.opensymphony.xwork2.ActionContext;

import steed.action.BaseAction;
import steed.exception.runtime.system.FrameworkException;
import steed.hibernatemaster.domain.BaseDatabaseDomain;
import steed.util.base.DomainUtil;
import steed.util.base.StringUtil;
import steed.util.reflect.ReflectUtil;
/**
 * 把传来的string数组转换成domain多对多或一对多中的set
 * set必须含有泛型
 * @author 战马
 *
 */
public class CollectionsConverter extends StrutsTypeConverter {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		String name = (String) context.get("conversion.property.fullName");
		List<Object> list = getList(name, values);
		if (toClass.isAssignableFrom(Set.class)) {
			return new HashSet<>(list);
		}else if (toClass.isAssignableFrom(List.class)) {
			return list;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private List<Object> getList(String domainName,String[] values){
//		String fullDomainName = getFullDomainName(domainName);
		List<Object> list;
		Class<?> domainClass = getDomainNameClass(domainName);
		if (ReflectUtil.isClassBaseID(domainClass)) {
			list = evalString2BaseID(domainClass, values);
		}else {
			list = evalString2Domain((Class<? extends BaseDatabaseDomain>)domainClass, values);
		}
		return list;
	}
	
	private List<Object> evalString2BaseID(Class<?> domainClass,String[] eval){
		List<Object> domainList = new ArrayList<>();
		for (String str:eval) {
			if (!StringUtil.isStringEmpty(str)) {
				domainList.add(ReflectUtil.string2BaseID(domainClass, str));
			}
		}
		return domainList;
	}
	private List<Object> evalString2Domain(Class<? extends BaseDatabaseDomain> domainClass,String[] eval){
		List<Object> domainList = new ArrayList<>();
		try {
			String iDname = DomainUtil.getDomainIDName(domainClass);
			Field idField = domainClass.getDeclaredField(iDname);
			idField.setAccessible(true);
			for (String str:eval) {
				if (!StringUtil.isStringEmpty(str)) {
					Object domain = domainClass.getConstructor().newInstance(null);
					Class<?> fieldType = idField.getType();
					Object ID = ReflectUtil.string2BaseID(fieldType, str);
					idField.set(domain, ID);
					domainList.add(domain);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("在"+domainClass.getName()+"中找不到参数为空的public构造函数", e);
		} 
		return domainList;
	}

	private Class<?> getDomainNameClass(String domainName) {
		
		BaseAction<?> action = (BaseAction<?>) ActionContext.getContext().getActionInvocation().getAction();
		Field f;
		try {
			f = action.getModel().getClass().getDeclaredField(domainName);
		} catch (NoSuchFieldException e1) {
			e1.printStackTrace();
			throw new FrameworkException(e1);
		} catch(SecurityException e){
			e.printStackTrace();
			throw new FrameworkException(e);
		}
		return ReflectUtil.getGenericType(f);
		
		/*String fullDomainName;
		fullDomainName = PropertyUtil.getProperties("domainMap.properties").getProperty(domainName);
		if (fullDomainName == null) {
			throw new RuntimeException("在domainMap.properties中找不到"+domainName+"的相关配置");
		}
		return fullDomainName;*/
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String convertToString(Map context, Object obj) {
		return null;
	}

}
