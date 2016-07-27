package steed.action.intercepter;

import org.apache.log4j.Logger;

import steed.exception.MessageException;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
/**
 * 把抛出的异常放到request域中,让异常页面能把异常信息取出来
 * @author battle_steed
 *
 */
public class MessageIntercepter extends AbstractInterceptor {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(MessageIntercepter.class);
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		try {
			String invoke = invocation.invoke();
			/*if (BaseAction.steed_forward.equals(invoke)) {
				return null;
			}*/
			return invoke;
		} catch (Exception e) {
			ActionContext actionContext = invocation.getInvocationContext();
			actionContext.put("exception", e);
			e.printStackTrace();
			if (e instanceof MessageException) {
				logger.debug(e.getMessage(),e);
			}else {
				logger.error(e.getMessage(),e);
			}
			throw e;
		}
	}
}
