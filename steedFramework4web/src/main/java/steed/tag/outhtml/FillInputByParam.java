package steed.tag.outhtml;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
/**
 * 作用自己意会
 * @author 战马
 *
 */
public class FillInputByParam extends TagSupport{
	private static final long serialVersionUID = -5584256537704686928L;
	private String root = "$(document)";

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
			JspWriter out = pageContext.getOut();
			out.print("<script type=\"text/javascript\">");
			out.print("var ");
			String name = "fillInputByParam";
			out.print(name);
			out.print(" = ");
			out.print(root);
			out.print(";\n");
			Enumeration<String> parameterNames = req.getParameterNames();
			while (parameterNames.hasMoreElements()) {
				String string = (String) parameterNames.nextElement();
				out.print(getScript(req.getParameter(string), string,name));
			}
			out.print("</script>");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}
	
	private String getScript(String value,String name,String rootName){
		String compatibilityName = (rootName+name).replaceAll("\\.", "_____");
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
