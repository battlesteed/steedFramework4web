package steed.util.system;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import steed.domain.BaseDomain;
import steed.domain.GlobalParam;
import steed.domain.annotation.AllowSpecialCharacter;
import steed.domain.annotation.CleanXss;
import steed.domain.annotation.ValidateReg;
import steed.exception.ValidateException;
import steed.util.base.BaseUtil;
import steed.util.base.PropertyUtil;
import steed.util.base.StringUtil;
/**
 * 验证数据是否合法工具类
 * @author 战马
 *
 */
public class ValidateUtil {
	private static Logger logger = LoggerFactory.getLogger(ValidateUtil.class);
	private static String[] strNotArrow = {"^[\\s\\S]*expression([\\s\\S]*)[\\s\\S]*$","^[\\s\\S]*javascript:[\\s\\S]*$"};
	
	
	/**
	 * 校验对象中的f
	 * @param obj
	 * @param f
	 * @return
	 * @throws ValidateException 数据校验不通过时抛出
	 */
	private static Object validate(Object obj,Field f) throws ValidateException{
		if (BaseUtil.isObjEmpty(obj)) {
			return obj;
		}
		if(obj instanceof BaseDomain) {
			return validateObj(obj);
		}else if (obj instanceof Collection) {
			return validateCollection(obj);
		}else if (obj instanceof Map) {
			return validateMap(obj);
		}else {
			if (f != null) {
				ValidateReg validateReg = f.getAnnotation(ValidateReg.class);
				if (validateReg != null) {
					String[] regs = validateReg.reg();
					for(String str:regs){
						if (!obj.toString().matches(str)) {
							throwValidateException(obj);
						}
					}
				}
			}
			
			if(obj instanceof String){
				if (f == null) {
					return trensferrSpecailCharacter((String) obj);
				}else {
					if (f.getAnnotation(CleanXss.class) != null) {
						String cleaned = cleanXss((String)obj);
						return cleaned;
					}else if (f.getAnnotation(AllowSpecialCharacter.class) == null) {
						return trensferrSpecailCharacter((String) obj);
					}
				}
			}
		}
		return obj;
	}
	/**
	 * 把'替换成＇,转义str中的<>&",防止跨站脚本攻击,
	 * 如果是富文本请用cleanXss
	 * @param str
	 * @return
	 */
	public static Object trensferrSpecailCharacter(String str) {
		return StringUtil.transferrCharacter((str).replaceAll("'", "＇"));
	}
	/**
	 * 清除富文本内容中的跨站脚本攻击字符,
	 * 如果不是富文本请用trensferrSpecailCharacter
	 * @param str
	 * @return
	 */
	public static String cleanXss(String str) {
		Whitelist relaxed = Whitelist.relaxed();
		relaxed.addAttributes(":all", "style");
		relaxed.addAttributes(":all", "class");
		relaxed.addAttributes("iframe", "src");
		relaxed.addAttributes("iframe", "height");
		relaxed.addAttributes("iframe", "width");
		relaxed.addTags("iframe");
		String clean = Jsoup.clean(str, PropertyUtil.getConfig("site.rootURL"),relaxed);
		Document doc = Jsoup.parse(clean);
		
		validateIframe(doc);
		
		validateNode(doc);
		return doc.outerHtml();
	}
	
	private static void validateIframe(Document doc) {
		Elements select = doc.select("iframe");
		Iterator<Element> iterator = select.iterator();
		while (iterator.hasNext()) {
			Element next = iterator.next();
			if ("iframe".equals(next.nodeName())) {
				String src = next.attr("src");
				if (!StringUtil.isStringEmpty(src)) {
					if (!src.startsWith(String.format("%s%s", GlobalParam.FOLDER.contextUrl,"/plugin/ueditor/"))) {
						next.remove();
					}
				}
			}
		}
	}
	
	private static void validateNode(Node n){
		for (Node node:n.childNodes()) {
			for(Attribute a:node.attributes().asList()){
				String value = a.getValue();
				for (String str:strNotArrow) {
					if (value.matches(str)) {
						node.removeAttr(a.getKey());
						break;
					}
				}
			}
			validateNode(node);
		}
	}
	
	/**
	 * 校验普通对象（map，Collection请调用其他校验方法）(是否符合字段的注解,转义字符等)
	 * @param obj
	 * @return
	 * @throws ValidateException 数据校验不通过时抛出
	 */
	public static Object validateObj(Object obj) throws ValidateException{
		Field[] fields = obj.getClass().getDeclaredFields();
		for(Field f:fields){
			String fieldName = f.getName();
			if ("serialVersionUID".equals(fieldName)) {
				continue;
			}
			f.setAccessible(true);
			try {
				f.set(obj, validate(f.get(obj), f));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return obj;
	}
	
	/**
	 * 校验map(是否符合字段的注解,转义字符等)
	 * @param obj
	 * @return
	 * @throws ValidateException 数据校验不通过时抛出
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object validateMap(Object obj) throws ValidateException {
		Map<Object,Object> map = (Map)obj;
		try {
			Map<Object,Object> chache = map.getClass().newInstance();
			for(Entry<Object, Object> e:map.entrySet()){
				chache.put(e.getKey(), validate(e.getValue(),null));
			}
			return chache;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 校验Collection(是否符合字段的注解,转义字符等)
	 * @param obj
	 * @return
	 * @throws ValidateException 数据校验不通过时抛出
	 */
	@SuppressWarnings("unchecked")
	public static Object validateCollection(Object obj) throws ValidateException {
		Collection<Object> collection = (Collection<Object>)obj;
		Iterator<Object> iterator = collection.iterator();
		System.out.println(collection.getClass().getName());
		while (iterator.hasNext()) {
			Object next = iterator.next();
			validate(next,null);
		}
		return obj;
	}
	
	private static void throwValidateException(Object temp) throws ValidateException {
		String message = String.format("%s校验不通过！！！值：%s", temp.getClass().getName(),String.valueOf(temp));
		ValidateException validateException = new ValidateException(message);
		logger.warn(message, validateException);
		throw validateException;
	}
}
