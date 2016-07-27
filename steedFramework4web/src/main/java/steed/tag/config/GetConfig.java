package steed.tag.config;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import steed.util.base.PropertyUtil;
import steed.util.base.StringUtil;

public class GetConfig extends TagSupport{
	private static final long serialVersionUID = -6163891527356079693L;
	private String key;

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public int doStartTag() throws JspException {
		JspWriter out = pageContext.getOut();
		try {
			out.print(StringUtil.getNotNullString(PropertyUtil.getProperties("config.properties").getProperty(key)));
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

}
