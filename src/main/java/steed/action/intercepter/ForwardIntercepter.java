package steed.action.intercepter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.convention.annotation.Namespace;

import steed.action.BaseAction;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.config.ConfigurationException;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
/**
 * forward与action中方法相对应的jsp,要求类位置和jsp页面位置一定要对应
 * @author 战马
 *
 */
public class ForwardIntercepter extends AbstractInterceptor {
	private static final long serialVersionUID = -6358178758962004165L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		try {
			String temp = invocation.invoke();
			if(BaseAction.steed_forward.equals(temp)){
				forWard(invocation);
				return null;
			}
			return temp;
		} catch (ConfigurationException e) {
			String message = e.getMessage();
			if (message.contains(BaseAction.steed_forward)) {
				forWard(invocation);
			}else {
				throw e;
			}
		}
		return null;
	}

	private void forWard(ActionInvocation invocation) throws ServletException,
			IOException {
		BaseAction action = (BaseAction) invocation.getAction();
		HttpServletRequest request = action.getRequest();
		StringBuffer path = new StringBuffer("/WEB-INF/jsp");
		Class<? extends BaseAction> actionClass = action.getClass();
		Namespace namespace = actionClass.getAnnotation(Namespace.class);
		String folder; 
		if (namespace == null) {
			folder="/";
//			String actionFullName = actionClass.getName();
//			folder = actionFullName.substring(actionFullName.indexOf(".action."),actionFullName.length());
//			folder = folder.replace(".action", "").replaceAll("\\.", "/")
//					.substring(0,folder.length()-6).toLowerCase()+"/";
		}else {
			folder = namespace.value()+"/";
		}
		path.append(folder);
		path.append(invocation.getProxy().getMethod());
		path.append(".jsp");
		request.getRequestDispatcher(path.toString()).forward(request, action.getResponse());
	}

}
