package steed.tag.outhtml;

import java.io.IOException;


import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import steed.util.base.PathUtil;

/**
 * PathUtil.getBrowserPath(relativeUrl)
 * @author 战马
 */
public class GetBrowserPath extends TagSupport {
	private static final long serialVersionUID = -1523362354887392103L;
	private String relativeUrl;
	public void setRelativeUrl(String relativeUrl) {
		this.relativeUrl = relativeUrl;
	}
	@Override
	public int doStartTag() throws JspException {
		 try {
			JspWriter out = pageContext.getOut();
			out.print(PathUtil.getBrowserPath(relativeUrl));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}
}
