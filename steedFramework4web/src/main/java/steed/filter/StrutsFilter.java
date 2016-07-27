package steed.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.dispatcher.filter.StrutsPrepareAndExecuteFilter;

public class StrutsFilter extends StrutsPrepareAndExecuteFilter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		try {
			if(((HttpServletRequest)request).getRequestURI().endsWith("ueditorController.jsp")){
				chain.doFilter(request, response);
			}else {
				super.doFilter(request, response, chain);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*try {
			BaseUtil.out("struts");
			super.doFilter(request, response, arg2);
		} catch (Exception e) {
			e.printStackTrace();
			BaseUtil.out("抓到异常");
			if (e instanceof MessageException) {
				request.setAttribute("message", ((MessageException)e).getMsg());
			}
			request.setAttribute("exception", e);
			request.getRequestDispatcher("/WEB-INF/jsp/message.jsp").forward(request, response);
		}*/
	}

}
