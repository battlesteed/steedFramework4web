package steed.util.system;

import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import steed.domain.GlobalParam;
import steed.domain.application.DWZMessage;
import steed.domain.application.Message;
import steed.domain.system.Config;
import steed.ext.domain.system.LoginLog;
import steed.ext.domain.user.User;
import steed.servlet.SecurityCode;
import steed.util.base.DateUtil;
import steed.util.base.StringUtil;
import steed.util.dao.DaoUtil;
import steed.util.digest.AESUtil;
import steed.util.digest.DigestUtil;

public class LoginUtil {
	public Message login(HttpServletRequest request) {
		String securityCode = request.getParameter("securityCode");
		Message message = new Message();
		try {
			
			if (StringUtil.isStringEmpty(securityCode)) {
				message.setErrCode(1);
				message.setMessage("验证码不能为空！！！");
				return message;
			}
			
			HttpSession session = request.getSession();
			if (!securityCode.toUpperCase().equals(session.getAttribute(SecurityCode.RANDOMCODEKEY))) {
				message.setErrCode(2);
				message.setMessage("验证码错误！");
				session.setAttribute(SecurityCode.RANDOMCODEKEY, String.valueOf(new Random().nextLong()));
				return message;
			}
			session.setAttribute(SecurityCode.RANDOMCODEKEY, String.valueOf(new Random().nextLong()));
			
			/*float maxLoginMinute = Float.parseFloat(BaseDao.get(Config.class, "登陆错误超标重新登陆等待时间").getValue());
			int maxLoginCount = Integer.parseInt(BaseDao.get(Config.class,"最多登陆错误次数").getValue());
			
			Date now = new Date();
			LoginLog log = new LoginLog();
			log.setLoginDate_max_1(now);
			log.setLoginDate_min_1(DateUtil.getLastXminiDate(now, maxLoginMinute));
			//log.setNickName(((People)request.getSession().getAttribute("user")).getNickName());
			log.setRemoteAddr(request.getRemoteAddr());
			long loginCoutn = BaseDao.getCount(log);
			if (loginCoutn > maxLoginCount) {
				message.setErrCode(-1);
				message.setMessage("您"+(int)maxLoginMinute+"分钟内登录次数已超过"+maxLoginCount+"次。。。-_-|||");
				return message;
			} */
		
			String nickName = request.getParameter("nickName");
			String password = AESUtil.aesEncode(request.getParameter("password"));
			
			User people = DaoUtil.get(User.class, nickName);
			
			if (people == null) {
				message.setErrCode(4);
				message.setMessage("用户不存!");
				return message;
			}
			
			String remoteAddr = request.getRemoteAddr();
			if (!people.getPassword().equals(password)) {
				message.setErrCode(3);
				message.setMessage("用户名或密码错误!");
				
				makeLoginLog(remoteAddr, nickName,"<span style=\"color:#ff0000;\">密码错误！</span>",3);
				
				return message;
			}
			
			message.setErrCode(0);
			message.setMessage("登录成功！！！");
			people.initializeAll();
			session.setAttribute(GlobalParam.attribute.user, people);
			session.setAttribute(GlobalParam.attribute.admin, people);
			
			makeLoginLog(remoteAddr, nickName,"登陆成功！！！",0);
			
		} catch (Exception e) {
			message.setErrCode(-2);
			message.setMessage("因不明原因，登录失败!");
			e.printStackTrace();
		}
		return message;
	}
	
	public DWZMessage commonUserLogin(HttpServletRequest request) {
		DWZMessage message = new DWZMessage();
		try {
			
			String remoteAddr = request.getRemoteAddr();
			if (isLoginCountOverproof(remoteAddr)) {
				message.setStatusCode(300);
				message.setMessage("您"+(int)getMaxLoginMin()+"分钟内登录次数已超过"+getMaxLoginCount()+"次,请稍后重试");
				return message;
			} 
		
			String nickName = request.getParameter("nickName");
			String password = DigestUtil.AESAndMd532MinEncode(request.getParameter("password"));
			
			User people = DaoUtil.get(User.class, nickName);
			
			if (people == null) {
				message.setStatusCode(4);
				message.setMessage("用户不存在!");
				return message;
			}
			
			if (!people.getPassword().equals(password)) {
				message.setStatusCode(3);
				message.setMessage("用户名或密码错误!");
				
				makeLoginLog(remoteAddr, nickName,"<span style=\"color:#ff0000;\">密码错误！</span>",3);
				
				return message;
			}
			
			message.setStatusCode(200);
			message.setMessage("登录成功！！！");
			people.initializeAll();
			
			makeLoginLog(remoteAddr, nickName,"登陆成功！！！",0);
			
		} catch (Exception e) {
			message.setStatusCode(-2);
			message.setMessage("因不明原因，登录失败!");
			e.printStackTrace();
		}
		return message;
	}

	public static float getMaxLoginMin() {
		float maxLoginMinute = Float.parseFloat(DaoUtil.get(Config.class, "登陆错误超标重新登陆等待时间").getValue());
		return maxLoginMinute;
	}

	
	/**
	 * 登录次数是否超标
	 * @param request
	 * @param maxLoginMinute
	 * @return
	 */
	public static boolean isLoginCountOverproof(String remoteAddr) {
		
		int maxLoginCount = getMaxLoginCount();
		
		long loginCoutn = getLoginErrorCount(remoteAddr);
		return loginCoutn > maxLoginCount;
	}

	public static int getMaxLoginCount() {
		int maxLoginCount = Integer.parseInt(DaoUtil.get(Config.class,"最多登陆错误次数").getValue());
		return maxLoginCount;
	}
	
	public static long getLoginErrorCount(String remoteAddr) {
		Date now = new Date();
		float maxLoginMinute = getMaxLoginMin();
		LoginLog log = new LoginLog();
		log.setLoginDate_max_1(now);
		log.setLoginDate_min_1(DateUtil.getLastXminiDate(now, maxLoginMinute));
		log.setRemoteAddr(remoteAddr);
		log.setLoginResult_not_equal_1(0);
		long loginCoutn = DaoUtil.getCount(log);
		return loginCoutn;
	}

	public static void makeLoginLog(String remoteAddr, String nickName,
			String message,int loginResult){
		LoginLog loginLog = new LoginLog();
		loginLog.setNickName(nickName);
		loginLog.setRemoteAddr(remoteAddr);
		loginLog.setLoginDate(new Date());
		loginLog.setMessage(message);
		loginLog.setLoginResult(loginResult);
//		Hashtable<String,String> hashtable = new QueryIP().query2(remoteAddr);
//		loginLog.setAddress(hashtable.get("COUNTRY"));
//		loginLog.setArea(hashtable.get("AREA"));
		loginLog.save();
	}
}
