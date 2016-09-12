package steed.action.intercepter;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import steed.exception.ValidateException;
import steed.util.base.BaseUtil;
/**
 * 在拦截器栈中引用该拦截器后，action中的方法只要加了@Action注解
 * 即可被“Namespace+方法名+struts后缀”所调用，本来打算@Action注解都不用加的，
 * 但考虑到安全问题，方法必须加@Action注解。
 * @author 战马
 *
 */
public class MethodInvokIntercepter extends AbstractInterceptor {
	private static final long serialVersionUID = -5530892141722204927L;
	@Override
	public String intercept(ActionInvocation invocation) throws Exception{
//		BaseAction<?> baseAction = (BaseAction<?>) invocation.getAction();
		try {
			BaseUtil.out("MethodInvokIntercepter");
//			ValidateUtil.validateObj(baseAction);
			return invocation.invoke();
		} catch (ValidateException e) {
			e.printStackTrace();
			BaseUtil.out(e);
			return null;
		}
	}

}
