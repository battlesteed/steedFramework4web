package steed.tag.outhtml;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
/**
 * 打印空格
 * @author 战马
 *
 */
public class Space extends TagSupport {
	private static final long serialVersionUID = -1523362354887392103L;
	private int spaceCount;
	public void setSpaceCount(int spaceCount) {
		this.spaceCount = spaceCount;
	}
	@Override
	public int doStartTag() throws JspException {
		 try {
			JspWriter out = pageContext.getOut();
			for (int i = 0; i < spaceCount; i++) {
				 out.print("&nbsp;");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}
}
