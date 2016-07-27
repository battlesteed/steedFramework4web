package steed.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 基础servlet，所以servlet均需继承该类
 * @author 战马
 *
 */
public abstract class BaseServlet extends HttpServlet {
	private static final long serialVersionUID = -4198881773928934197L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

}
