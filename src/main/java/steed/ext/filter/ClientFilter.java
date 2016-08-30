package steed.ext.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import steed.ext.domain.terminal.ClientMessage;
import steed.ext.domain.terminal.TerminalUser;
import steed.util.base.PropertyUtil;
import steed.util.base.StringUtil;
import steed.util.dao.DaoUtil;
import steed.util.digest.AESUtil;
import steed.util.digest.Md5Util;
import steed.util.http.HttpUtil;
import steed.util.system.DataCacheUtil;
/**
 * 终端登录过滤器，请放在struts过滤器前面
 * @author 战马
 * @email java@beyondstar.com.cn 
 *        battle_steed@163.com
 * @company 深圳市星超越科技有限公司
 */
public class ClientFilter implements Filter{
//	private Logger logger = LoggerFactory.getLogger(BaseAdminFilter.class);
	
	@Override
	public void destroy() {
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		String URI = ((HttpServletRequest) request).getRequestURI();
		if(URI.endsWith("keepSession.act")){
			HttpUtil.writeJson((HttpServletResponse) response, "1");
		}else if(URI.endsWith("client/getToken.act")){
			getToken((HttpServletRequest)request, (HttpServletResponse)response);
		}else if (PropertyUtil.getBoolean("devMode")&& "dev".equals(request.getParameter("token"))) {
			if (DataCacheUtil.getData("dev", "TerminalToken") == null) {
				DataCacheUtil.setData("dev", "TerminalToken", DaoUtil.get(TerminalUser.class, "admin"));
			}
			chain.doFilter(request, response);
		}else {
			String token = request.getParameter("token");
			if (StringUtil.isStringEmpty(token)) {
				  ClientMessage message = new ClientMessage();
				  message.setStatusCode(-1);
				  message.setMessage("请求非法！");
				  HttpUtil.writeJson((HttpServletResponse) response, message);
			}else {
				if (DataCacheUtil.getData(token, "TerminalToken") == null) {
					ClientMessage message = new ClientMessage();
					message.setStatusCode(-2);
					message.setMessage("Token过期！");
					HttpUtil.writeJson((HttpServletResponse) response, message);
				}else {
					chain.doFilter(request, response);
				}
			}
		}
	}
	
	/**
     * 终端获取token
     * @author 战马
     * @param request
     * @param response
     */
    private void getToken(HttpServletRequest request, HttpServletResponse response) {
    	 //用户名
        String userid = request.getParameter("userid");
        //签名字符串
        String sign = request.getParameter("sign");
        //时间戳
        String timeStamp = request.getParameter("timeStamp");
        Date now = new Date();
        ClientMessage message = new ClientMessage();
        /**
         * 时间只允许有5分钟误差
         */
        long time = now.getTime();
		if (Math.abs(time - Long.parseLong(timeStamp)) > 1000*60*5) {
			message.setStatusCode(-501);
			message.setMessage("时间戳过期！！");
		}else {
			TerminalUser user = DaoUtil.get(TerminalUser.class, userid);
			if (user == null) {
				message.setStatusCode(-502);
				message.setMessage("用户不存在！！");
			}else {
				String signStr = Md5Util.Md5Digest(String.format("%s%s%s", userid,AESUtil.aesDecode(user.getPassword()),timeStamp));
				if (signStr.equals(sign)) {
					message.setStatusCode(0);
					message.setMessage("登录成功！！请保存您的Token。");
					String token = Md5Util.Md5Digest(StringUtil.getSecureRandomString());
					message.setContent(token);
					DataCacheUtil.setData(token, "TerminalToken", user);
					//tokeCache.put(token, time);
				}else {
					message.setStatusCode(-503);
					message.setMessage("登录失败！！sign参数不正确。");
				}
			}
		}
//		return message;
		HttpUtil.writeJson(response, message);
	}

}
