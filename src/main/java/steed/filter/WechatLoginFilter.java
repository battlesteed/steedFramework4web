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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import steed.domain.wechat.WechatUser;
import steed.hibernatemaster.util.DaoUtil;
import steed.util.base.ContextUtil;
import steed.util.base.PropertyUtil;
import steed.util.base.StringUtil;
import steed.util.wechat.WechatInterfaceInvokeUtil;

/**
 * 微信静默登录过滤器
 * @author 战马
 * @email battle_steed@163.com
 */
public class WechatLoginFilter implements Filter{
	Logger logger = LoggerFactory.getLogger(WechatLoginFilter.class);
	@Override
	public void destroy() {
		
	}
	
	public static boolean isBrowserWechat(HttpServletRequest request){
		String header = request.getHeader("User-Agent");
		if (header == null) {
			return false;
		}else {
			return header.toLowerCase().contains("micromessenger");
		}
	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		
		boolean login = true;
		if (PropertyUtil.getBoolean("wechat.loginOnlyOnwechat")) {
			login = isBrowserWechat(((HttpServletRequest)req));
		}
		
		if (login && ((HttpServletRequest)req).getRequestURI().endsWith(".act")) {
			WechatUser wechatUser = (WechatUser)((HttpServletRequest)req).getSession().getAttribute("wechatUser");
			if (wechatUser == null) {
				//开发需要，pc端登录
				if (PropertyUtil.getBoolean("devMode")&&
						!StringUtil.isStringEmpty(((HttpServletRequest)req).getParameter("openId"))) {
					ContextUtil.getSession().setAttribute("wechatUser", DaoUtil.get(WechatUser.class, ((HttpServletRequest)req).getParameter("openId")));
					chain.doFilter(req, resp);
					logger.debug("开发模式微信登录");
				}else{
					logger.debug("用户未登录登录，开始微信登录");
					WechatInterfaceInvokeUtil.getUserInformation((HttpServletResponse)resp,(HttpServletRequest)req, false,true);
				}
			}else {
				if (wechatUser.getSex() == null && PropertyUtil.getBoolean("wechat.pullAllinformation")) {
					logger.debug("开始拉取微信用户信息");
					WechatInterfaceInvokeUtil.getUserInformation((HttpServletResponse)resp,(HttpServletRequest)req, true,true);
				}else {
					logger.debug("微信用户已登录");
					chain.doFilter(req, resp);
				}
			}
		}else {
			chain.doFilter(req, resp);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
