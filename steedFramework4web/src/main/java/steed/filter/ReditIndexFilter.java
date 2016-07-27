package steed.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 重定向 类似xx/的链接到xx/index.act
 * @author 战马
 *
 */
public class ReditIndexFilter implements Filter {
	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
			HttpServletRequest req = (HttpServletRequest)request;
			if(sendRedirect2Index(req,(HttpServletResponse)response)){
				return;
			}
			chain.doFilter(req, response);
	}
	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private boolean sendRedirect2Index(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String url = request.getRequestURL().toString();
		if (url.endsWith("/")) {
			response.sendRedirect(url+"index.act");
			return true;
		}
		return false;
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		

	}

}
