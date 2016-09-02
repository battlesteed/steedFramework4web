package steed.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import steed.util.base.StringUtil;
public class encodUrl extends TagSupport{
	private static final long serialVersionUID = -5584256537704686928L;
	private String url;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public int doStartTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();
			out.print(StringUtil.encodeUrl(url));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

}
