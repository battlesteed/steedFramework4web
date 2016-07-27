package steed.other;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.dispatcher.StrutsRequestWrapper;

public class SteedHttpServletRequest extends StrutsRequestWrapper{

	public SteedHttpServletRequest(HttpServletRequest request) {
		super(request);
	}
	public Long getLong(String name){
		return Long.parseLong(getParameter(name));
	}
}
