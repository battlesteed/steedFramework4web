package steed.ext.action.admin.user;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;

import steed.action.annotation.Power;
import steed.ext.action.admin.DwzAdminAction;
import steed.ext.domain.user.User;
import steed.util.dao.DaoUtil;
@ParentPackage(value="steed")
@InterceptorRefs({@InterceptorRef(value="mydefault")})
@Namespace("/admin/user/userCenter")
@Power("个人信息管理")
public class UserCenterAction extends DwzAdminAction<User>{
	private static final long serialVersionUID = 7839375284635130117L;
//	@Model
//	private User member = DomainFactory.getDomain(User.class);
	@Action("index")
	@Power("查看个人信息")
	public String index(){
		setRequestDomain(DaoUtil.smartGet((User)getLoginUser()));
		return steed_forward;
	}
	
	@Action(value="update")
	@Power("编辑个人信息")
	public String update(){
		validateCurrentUser();
		domain.aesDecodePassword();
//		member.setPowerSet(null);
		domain.setRoleSet(null);
		return super.updateNotNullField(null,true);
	}
	
}
