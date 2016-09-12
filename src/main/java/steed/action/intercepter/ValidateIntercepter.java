package steed.action.intercepter;

import org.apache.log4j.Logger;

import steed.action.BaseAction;
import steed.exception.ValidateException;
import steed.exception.runtime.system.AttackedExeception;
import steed.util.system.ValidateUtil;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
/**
 * 校验action中的字段是否合法,并替换特殊字符
 * @author 战马
 *
 */
public class ValidateIntercepter extends AbstractInterceptor {
	private static final long serialVersionUID = -5530892141722204927L;
	private static Logger logger = Logger.getLogger(ValidateIntercepter.class);

	@Override
	public String intercept(ActionInvocation invocation) throws Exception{
		BaseAction<?> baseAction = (BaseAction<?>) invocation.getAction();
		try {
			ValidateUtil.validateObj(baseAction);
		} catch (ValidateException e) {
			e.printStackTrace();
			logger.info(e);
			throw new AttackedExeception(e);
		}
		return invocation.invoke();
	}

}
