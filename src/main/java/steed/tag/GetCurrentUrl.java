package steed.tag;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import steed.domain.GlobalParam;
import steed.exception.PathIsTopException;
import steed.util.base.PathUtil;
import steed.util.base.StringUtil;
import steed.util.file.FileUtil;
public class GetCurrentUrl extends TagSupport{
	private static final long serialVersionUID = -5584256537704686928L;
	private String lastUrl;

	public void setLastUrl(String lastUrl) {
		this.lastUrl = lastUrl;
	}

	@Override
	public int doStartTag() throws JspException {
		try {
			HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
			JspWriter out = pageContext.getOut();
			String path = req.getRequestURI().replaceAll(GlobalParam.FOLDER.JSPPATH, "/");
			if (StringUtil.isStringEmpty(lastUrl)) {
				out.print(FileUtil.replaceFileSuffix(path, ".act"));
			}else {
				out.print(PathUtil.getParaentPath(path));
				out.print("/");
				out.print(lastUrl);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (PathIsTopException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

}
