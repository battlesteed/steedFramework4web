package steed.ext.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import steed.domain.GlobalParam;
import steed.domain.application.DWZMessage;
import steed.ext.domain.user.User;
import steed.filter.BaseAdminFilter;
import steed.util.dao.DaoUtil;
import steed.util.digest.AESUtil;
import steed.util.http.HttpUtil;
import steed.util.system.LoginUtil;

public class AdminFilter extends BaseAdminFilter{

	@Override
	protected void login(HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		DWZMessage dwzMessage = new DWZMessage();
		if (LoginUtil.isLoginCountOverproof(request.getRemoteAddr())) {
			dwzMessage.setTitle("登录错误次数过多");
			dwzMessage.setMessage("您短时间内登录错误次数过多，请稍后再试。");
			dwzMessage.setStatusCode(DWZMessage.statusCode_fail);
		}else{
			
			User people = DaoUtil.get(User.class, request.getParameter("nickName"));
			if (people == null) {
				dwzMessage.setTitle("登录失败");
				dwzMessage.setMessage("用户不存在!");
				dwzMessage.setStatusCode(DWZMessage.statusCode_fail);
			}/*else if(people.getUserType() != 0){
				dwzMessage.setTitle("登录失败");
				dwzMessage.setMessage("您的用户类型不能登录该后台!");
				dwzMessage.setStatusCode(DWZMessage.statusCode_fail);
			}*/else if(!people.getPassword().equals(AESUtil.aesEncode(request.getParameter("password")))){
				dwzMessage.setData(people.getUserType());
				dwzMessage.setTitle("登录失败");
				dwzMessage.setMessage("用户名或密码错误");
				dwzMessage.setStatusCode(DWZMessage.statusCode_fail);
				LoginUtil.makeLoginLog(request.getRemoteAddr(), request.getParameter("nickName"), dwzMessage.getMessage(), 3);
			}else {
				dwzMessage.setData(people.getUserType());
				dwzMessage.setMessageAndTitle("登录成功");
				dwzMessage.setStatusCode(DWZMessage.statusCode_success);
				makeLogin(people,request.getSession());
				LoginUtil.makeLoginLog(request.getRemoteAddr(), request.getParameter("nickName"), dwzMessage.getMessage(), 3);
			}
		}
//		Message message = new LoginUtil().login(request);
		HttpUtil.writeJson(response, dwzMessage);
	}

	private void makeLogin(User people,HttpSession session) {
		people.initializeAll();
		session.setAttribute(GlobalParam.attribute.user, people);
		session.setAttribute(GlobalParam.attribute.admin, people);
	}

}
