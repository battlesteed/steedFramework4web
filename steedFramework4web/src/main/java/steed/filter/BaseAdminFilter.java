package steed.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import steed.domain.BaseUser;
import steed.domain.GlobalParam;
import steed.domain.application.Message;
import steed.exception.runtime.MessageRuntimeException;
import steed.util.base.PropertyUtil;

/**
 * 基础后台过滤类，如用户未登录则拦截除登录路径外所有/admin/下的访问。
 * 抽象类，需要实现其的登录方法。
 * @author 战马
 *
 */
public abstract class BaseAdminFilter implements Filter {
	
	private Logger log = LoggerFactory.getLogger(BaseAdminFilter.class);
	
	@Override
	public void destroy() {
		
	}
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		HttpSession session = request.getSession();
		BaseUser user = (BaseUser) session.getAttribute(GlobalParam.attribute.admin);
		
		String adminLoginPath = PropertyUtil.getConfig("site.adminLoginPath");
		String URI = request.getRequestURI();
		String adminPostLoginPath = PropertyUtil.getConfig("site.adminPostLoginPath");
		if(!URI.endsWith(adminLoginPath)){
			if(URI.endsWith(adminPostLoginPath)){
				login(request, response);
//				chain.doFilter(request, response);
			}else {
				if (user == null) {
					Message message;
					if (PropertyUtil.getBoolean("go2Login")) {
						message = new Message(3,"您还没登录，2s自动跳转登录页面...","."+adminLoginPath,"您还没有登录");
					}else {
						message = new Message(3,"您还没登录，请登录","您还没有登录");
					}
					throw new MessageRuntimeException(message);
				}else{
					if(user.hasPower("登录后台")){
						chain.doFilter(request, response);
					}else {
						Message message = new Message(2,"您没有<span style=\"color:ff0000\">登录后台</span>权限,请联系超级管理员。","您没有权限");
						throw new MessageRuntimeException(message);
					}
				}
			}
		}else{
			request.setAttribute("adminPostLoginPath", adminPostLoginPath);
			request.getRequestDispatcher("/WEB-INF/jsp/admin/login.jsp").forward(request, response);
		}
	}

	/**
	 * 请在这里编写登录代码,并把登录用户以steed.domain.GlobalParam.attribute.admin为
	 * key存放到session域
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	protected abstract void login(HttpServletRequest request, HttpServletResponse response)
			throws IOException;

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
