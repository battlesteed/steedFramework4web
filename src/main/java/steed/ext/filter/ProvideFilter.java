package steed.ext.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import steed.domain.BaseUser;
import steed.domain.GlobalParam;
import steed.domain.application.Message;
import steed.exception.runtime.MessageRuntimeException;
import steed.ext.domain.user.User;

public class ProvideFilter implements Filter{
//	private Logger logger = LoggerFactory.getLogger(BaseAdminFilter.class);
	
	@Override
	public void destroy() {
		
	}
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(GlobalParam.attribute.user);
		if(user == null || user.getUserType() != 2){
			Message message = new Message(3,"您还没登录，2s自动跳转登录页面...","./login.act","您还没有登录");
			throw new MessageRuntimeException(message);
		}else{
			chain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
