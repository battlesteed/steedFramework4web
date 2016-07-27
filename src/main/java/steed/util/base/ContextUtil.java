package steed.util.base;

import javax.servlet.http.HttpSession;

import steed.filter.DinamicFilter;
import steed.other.SteedHttpServletRequest;

public class ContextUtil {
	public static HttpSession getSession(){
		return getRequest().getSession();
	}
	public static SteedHttpServletRequest getRequest(){
		return new SteedHttpServletRequest(DinamicFilter.getRequest());
	}
}
