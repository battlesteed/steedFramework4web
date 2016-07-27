package steed.tag.outhtml;

import java.io.IOException;
import java.lang.reflect.Field;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import steed.domain.BaseDomain;
import steed.util.base.BaseUtil;
import steed.util.base.DomainUtil;
import steed.util.base.StringUtil;
/**
 * 作用自己意会
 * @author 战马
 *
 */
public class FillInputByDomain extends TagSupport{
	private static final long serialVersionUID = -5584256537704686928L;
	private boolean readOnlyID = true;
	private String root = "$(document)";
	private String attributeKey = "domain";
	private String[] fieldsSkip;
	private String prefix = "";
	public static final String[] strNotAll = new String[]{"<",">","\n","\"","'"};

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void setAttributeKey(String attributeKey) {
		this.attributeKey = attributeKey;
	}

	public void setReadOnlyID(boolean readOnlyID) {
		this.readOnlyID = readOnlyID;
	}

	public void setFieldsSkip(String fieldsSkip) {
		this.fieldsSkip = fieldsSkip.split("\\,");
	}

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = "$(document.getElementById(\""+root+"\"))";
	}

	@Override
	public int doStartTag() throws JspException {
		try {
			HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
			Object baseDomain = req.getAttribute(attributeKey);
			if (baseDomain == null) {
				return SKIP_BODY;
			}
			JspWriter out = pageContext.getOut();
			out.print("<script type=\"text/javascript\">");
			out.print("function fillInputByDomain(){\n");
			out.print("var ");
			String name = baseDomain.getClass().getName().replaceAll("\\.", "");
			out.print(name);
			out.print(" = ");
			out.print(root);
			out.print(";\n");
			//root = name;
			if (baseDomain != null) {
				for(Field f:baseDomain.getClass().getDeclaredFields()){
					try {
						f.setAccessible(true);
						Object object = f.get(baseDomain);
						if (!BaseUtil.isObjEmpty(object) && !isSkip(f.getName())) {
							out.print(getScript(object.toString().trim().replaceAll("\\\"", "\\\""), prefix + f.getName(),name));
						}
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				/*
				 * $("[name='appID']",root)[0].disabled = true;
				*/
				if (readOnlyID && baseDomain instanceof BaseDomain) {
					//out.print("$(\"[name='");
					//out.print("$(\"\")document.getElementsByName(\"");
					String domainIDName = DomainUtil.getDomainIDName((Class<? extends BaseDomain>) baseDomain.getClass());
//					out.print(domainIDName);
//					out.print("']\",");
//					out.print(root);
//					out.print(")[0].disabled = true;\n");
					
					out.print("var steedFrameWork");
					out.print(domainIDName);
					out.print(" = $(\"[name='");
					out.print(domainIDName);
					out.print("']\",");
					out.print(name);
					out.print(");\n");
					out.print("if(steedFrameWork");
					out.print(domainIDName);
					out.print(".length > 0){\n");
					out.print("steedFrameWork");
					out.print(domainIDName);
					out.print(".attr('readOnly','readOnly')");
					out.print("\n}\n");
				}
				out.print("\n}\n");
				out.print("fillInputByDomain();");
			}
			out.print("</script>");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}
	
	
	private boolean isSkip(String name){
		if (fieldsSkip != null) {
			for(String str:fieldsSkip){
				if (name.equals(str)) {
					System.out.println(name);
					return true;
				}
			}
		}
		return false;
	}
	
	private String getScript(String value,String name,String rootName){
		if (!StringUtil.isStringEmpty(value)) {
			for (String temp:strNotAll) {
				if (value.contains(temp)) {
					return "";
				}
			}
		}
		String compatibilityName = (rootName+name).replaceAll("\\.", "_");
		StringBuffer sb = new StringBuffer();
		sb.append("var ")
			.append(compatibilityName)
			.append(" = $(\"[name='")
			.append(name)
			.append("']\",")
			.append(rootName)
			.append(");\n")
			.append("if(")
			.append(compatibilityName)
			.append(".length > 0){\n")
			.append("var temp = ")
			.append(compatibilityName)
			.append("[0];\n")
			.append("var steed_framework_type = $(temp).attr('type');\n")
			.append("if(steed_framework_type == 'radio' || steed_framework_type == 'checkbox'){\n")
			.append(compatibilityName)
			.append(".each(function(){\nif(this.value == \"")
			.append(value)
			.append("\"){\nthis.checked=true;\nreturn false;  \n}\n});")
			.append("\n}else{\ntemp.value = '")
			.append(value)
			.append("';\n}\n}\n");
		return sb.toString();
	}
}
