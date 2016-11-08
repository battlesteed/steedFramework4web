package steed.ext.action.admin.user;

import java.util.HashSet;
import java.util.Set;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;

import steed.action.annotation.Power;
import steed.domain.application.DWZMessage;
import steed.ext.action.admin.DwzAdminAction;
import steed.ext.domain.user.Role;
import steed.ext.domain.user.User;
import steed.util.dao.DaoUtil;
@ParentPackage(value="steed")
@InterceptorRefs({@InterceptorRef(value="mydefault")})
@Namespace("/admin/user/role")
@steed.action.annotation.Power("管理角色")
public class RoleAction extends DwzAdminAction<Role>{
	private static final long serialVersionUID = 7839375284635130117L;
	
	@Action("add")
	@Power("添加角色")
	public String add(){
		return steed_forward;
	}
	@Action("index")
//	@Power("查看角色列表")
	public String index(){
		return super.index();
	}
	@Action(value="delete")
	@Power("删除角色")
	public String delete(){
		DWZMessage message;
		String name = domain.getName();
		
		domain = DaoUtil.get(Role.class, name);
		if (domain.getIsSystemRole()) {
			message = new DWZMessage(DWZMessage.type_delete_fail);
			message.setMessage(name + "&nbsp;是系统角色，不能删除o(╯□╰)o");
		}else {
			User p = new User();
			Set<Role> roleSet = new HashSet<Role>();
			roleSet.add(domain);
			p.setRoleSet(roleSet);
			if (!DaoUtil.isResultNull(p)) {
				message = new DWZMessage(DWZMessage.type_delete_fail);
				message.setMessage("该角色下存在用户，不能删除o(╯□╰)o");
			}else{
				message = new DWZMessage(DWZMessage.type_delete,domain.delete());
			}
		}
		writeObjectMessage(message);
		return null;
	}
	@Action(value="update")
	@Power("编辑角色")
	public String update(){
	/*	if (GlobalParam.SYSTEM.SUPER_ADMIN_NAME.equals(role.getName())) {
			Role role1 = DaoUtil.get(Role.class, role.getName());
			role = DomainUtil.fillDomain(role1, role,null);
		}*/
		writeUpdateMessage(domain.update());
		return null;
	}
	@Action(value="save")
	@Power("添加角色")
	public String save(){
		return super.save();
	}
	@Action("edit")
	@Power("编辑角色")
	public String edit(){
		return super.edit();
	}
}
