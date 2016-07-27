package steed.ext.action.client;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;

@ParentPackage(value="steed")
@InterceptorRefs({@InterceptorRef(value="mydefault")})
@Namespace("/client/demo")
public class DemoAction extends BaseClientAction{
	private static final long serialVersionUID = -5352784615787106126L;

	@Action("test")
	public String test(){
		writeClientMessage(0, null, "这是接口返回来的数据,可以是实体类");
		return null;
	}
}
