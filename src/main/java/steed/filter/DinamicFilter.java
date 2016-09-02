package steed.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import steed.domain.application.DWZMessage;
import steed.exception.MessageExceptionInterface;
import steed.other.SteedHttpServletRequest;
import steed.util.UtilsUtil;
import steed.util.base.PropertyUtil;
import steed.util.base.StringUtil;
import steed.util.dao.DaoUtil;
import steed.util.dao.HibernateUtil;
import steed.util.http.HttpUtil;
/**
 * 动态过滤器，过滤所有动态url
 * @author 战马
 *
 */
public class DinamicFilter implements Filter {
	private static Logger logger = LoggerFactory.getLogger(DinamicFilter.class);
	private static ThreadLocal<HttpServletRequest> threadLocal = new ThreadLocal<HttpServletRequest>();
	@Override
	public void destroy() {

	}
	
	public static HttpSession getSession(){
		return getRequest().getSession();
	}
	
	public static HttpServletRequest getRequest(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			if (request != null) {
				return request;
			}
		} catch (NullPointerException e) {
			return threadLocal.get();
		}
		return threadLocal.get();
	}
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		try {
			if (PropertyUtil.getBoolean("devMode")) {
				HttpServletRequest req = (HttpServletRequest)request;
				listCookies(req);
				listParam(request);
				logger.debug(String.format("请求url--->%s", req.getRequestURL()));
			}
			threadLocal.set((HttpServletRequest) request);
			if (PropertyUtil.getBoolean("wholeManagTransaction")) {
				DaoUtil.setAutoManagTransaction(false);
				HibernateUtil.setCloseSession(false);
			}
			chain.doFilter(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			HttpServletRequest req = (HttpServletRequest)request;
			request.setAttribute("exception", e);
			boolean isAjax = !StringUtil.isStringEmpty(req.getParameter("ajax"));
			if (e instanceof MessageExceptionInterface) {
				if (isAjax) {
					responseExceptionJson(response, e);
				}else {
					request.getRequestDispatcher("/WEB-INF/jsp/public/message.jsp").forward(request, response);
					logger.debug(e.getMessage(),e);
				}
			}else {
				if (isAjax) {
					responseExceptionJson(response, e);
				}else {
					request.getRequestDispatcher("/WEB-INF/jsp/public/message2.jsp").forward(request, response);
					logger.error(e.getMessage(),e);
				}
			}
		}finally{
			try {
				if (PropertyUtil.getBoolean("wholeManagTransaction")
						&& DaoUtil.getTransactionType() != null
						&& DaoUtil.getTransaction() != null) {
					DaoUtil.managTransaction();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			UtilsUtil.releaseUtils();
			threadLocal.remove();
		}
	}
	private void responseExceptionJson(ServletResponse response, Exception e)
			throws IOException, UnsupportedEncodingException {
		DWZMessage dwzMessage = new DWZMessage();
		dwzMessage.setStatusCode(300);
		dwzMessage.setTitle("出错啦！！");
		dwzMessage.setMessage(e.getMessage());
		HttpUtil.writeJson((HttpServletResponse) response, dwzMessage);
	}
	
	private void listParam(ServletRequest req) {
		List<String> keyList = Collections.list(req.getParameterNames());
		if (!keyList.isEmpty()) {
			logger.debug("-----------参数-----------");
			for (String s : keyList) {
				String[] parameterValues = req.getParameterValues(s);
				System.out.print(s + "----->");
				for(String str:parameterValues){
					System.out.print(str+"   ");
				}
			}
			logger.debug("");
			logger.debug("-----------参数-----------");
		}
	}
	private void listCookies(HttpServletRequest req) {
		Cookie[] cookies = req.getCookies();
		if (cookies != null && cookies.length > 0) {
			logger.debug("-----------cookies-----------");
			for (Cookie c : cookies) {
				logger.debug(String.format("%s------>%s", c.getName(), c.getValue()));
			}
			logger.debug("-----------cookies-----------");
		}
	}


	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
