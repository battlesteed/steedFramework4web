package steed.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;

import steed.domain.GlobalParam;
/**
 * 
 * @author 战马
 *
 * @email battle_steed@163.com
 */
@Namespace("")
@ParentPackage(value="steed")
@InterceptorRefs({@InterceptorRef(value="mydefault")})
public class BaseIndexAction extends BaseAction{
	private static final long serialVersionUID = 4395374259028028362L;
	
	@Action("index")
	public String index(){
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		String fileName = GlobalParam.FOLDER.JSPPATH + req.getRequestURI().toString().
				replaceFirst(GlobalParam.FOLDER.contextPath+"/", "").replace(".act", ".jsp");
		try {
			req.getRequestDispatcher(fileName).forward(req, resp);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
