package steed.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import steed.domain.GlobalParam;
/**
 * jsp转发servlet，实现不用写action即可访问到受保护的jsp,
 * 路径后缀是.htm则会触发该servlet,
 * 漏洞：可访问任意jsp，不用
 * @author 战马
 *
 */
@Deprecated
public class JspForwardServlet extends BaseServlet{
	private static final long serialVersionUID = -7914240262517622426L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String fileName = GlobalParam.FOLDER.JSPPATH + req.getRequestURI().toString().
				replace(GlobalParam.FOLDER.contextPath+"/", "").replace(".htm", ".jsp");
		req.getRequestDispatcher(fileName).forward(req, resp);
	}

}
