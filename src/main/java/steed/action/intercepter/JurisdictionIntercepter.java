package steed.action.intercepter;

import java.lang.reflect.Method;

import steed.action.BaseAction;
import steed.action.annotation.Power;
import steed.domain.BaseUser;
import steed.domain.application.Message;
import steed.exception.runtime.MessageRuntimeException;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
/**
 * 权限校验
 * @author 战马
 *
 */
public class JurisdictionIntercepter extends AbstractInterceptor {
	private static final long serialVersionUID = 6513540483777206200L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		ActionProxy proxy = invocation.getProxy();
		BaseAction action = (BaseAction) proxy.getAction();
		action.getRequest().getRequestURI();
		Class<? extends Object> actionClass = action.getClass();
		BaseUser user = action.getLoginUser();
		checkJurisdiction(actionClass.getAnnotation(Power.class), user);
		Method method = actionClass.getDeclaredMethod(proxy.getMethod(), null);
		checkJurisdiction(method.getAnnotation(Power.class), user);
//		checkLinkMethod(actionClass, user, method);
		String invoke = invocation.invoke();
		/*if (BaseAction.steed_forward.equals(invoke)) {
			return null;
		}*/
		return invoke;
	}
	
	/**
	 * 无发表达这个方法是做什么的，只能举例，例如：当前访问的是编辑角色信息的页面，
	 * 该页面不需要权限
	 * 但该用户没有更新角色信息的权限，则不应该给他跳转到编辑角色信息的页面
	 * @param actionClass
	 * @param user
	 * @param method
	 * @throws NoSuchMethodException
	 */
	/*private void checkLinkMethod(Class<? extends Object> actionClass,
			BaseUser user, Method method) throws NoSuchMethodException {
		if (method.getName().equals("add")) {
			Method saveMethod = actionClass.getDeclaredMethod("save");
			if (saveMethod != null) {
				checkJurisdiction(saveMethod.getAnnotation(Power.class), user);
			}
		}
	}*/

	private void checkJurisdiction(Power actionPower, BaseUser user) {
		if (actionPower != null) {
			String powerName = actionPower.value();
			if (user == null && Power.logined.equals(powerName)) {
				Message message = new Message(101,"您没有登录,请登录。","您没有登录");
				throw new MessageRuntimeException(message);
			}
			if (!user.hasPower(powerName)) {
				Message message = new Message(102,"您没有<span style=\"color:ff0000\">"+powerName+"</span>权限,请联系超级管理员。","您没有权限");
				throw new MessageRuntimeException(message);
			}
		}
	}

}
