package steed.ext.action.admin.system;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;

import steed.domain.application.DWZMessage;
import steed.ext.action.admin.DwzAdminAction;
@ParentPackage(value="steed")
@InterceptorRefs({@InterceptorRef(value="mydefault")})
@Namespace("/admin/system/config")
//@Power("系统参数管理")
public class ConfigAction extends DwzAdminAction{
	private static final long serialVersionUID = 2917184309474601008L;

	@Action("index")
//	@Power("系统参数设置")
	public String index(){
		return steed_forward;
	}
	@Action(value="update")
//	@Power("系统参数设置")
	public String update(){
		
		writeObjectMessage(new DWZMessage(DWZMessage.type_update));
		return null;
	}
	
}
