package steed.tag.outhtml;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import steed.util.base.PropertyUtil;
/**
 * 获取url.properties中的url配置，统一网站导航，实现url一改全改
 * @author 战马
 */
public class Url extends TagSupport{
	private static final long serialVersionUID = -5584256537704686928L;
	private String url;
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public int doStartTag() throws JspException {
		try {
//			StringBuffer sb = new StringBuffer();
//			sb.append(PropertyUtil.getProperties("url.properties").get(url));
//			sb.append("?");
//			sb.append("from=");
//			HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
//			People user = (People) request.getSession().getAttribute(GlobalParam.attribute.user);
			/*if (user != null) {
				sb.append(user.getNickName());
			}else {
				sb.append(StringUtil.getNotNullString(request.getParameter("from")));
			}*/
			pageContext.getOut().print(PropertyUtil.getProperties("url.properties").get(url));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

}
