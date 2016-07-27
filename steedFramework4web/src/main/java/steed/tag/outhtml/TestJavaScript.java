package steed.tag.outhtml;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class TestJavaScript extends TagSupport {
	private static final long serialVersionUID = -1523362354887392103L;
	private String innerString = "测试";

	public void setInnerString(String innerString) {
		this.innerString = innerString;
	}

	@Override
	public int doStartTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();
			out.print("<h1 onclick=\"javascript:");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_BODY_INCLUDE;
	}


	@Override
	public int doEndTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();
			out.print("\">");
			out.print(innerString);
			out.print("</h1>");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}
	
}
