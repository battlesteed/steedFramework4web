package steed.ext.action.admin.user;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;

import steed.action.annotation.Power;
import steed.ext.action.admin.DwzAdminAction;
import steed.ext.domain.user.Role;
import steed.ext.domain.user.User;
import steed.hibernatemaster.util.DaoUtil;

@ParentPackage(value="steed")
@InterceptorRefs({@InterceptorRef(value="mydefault")})
@Namespace("/admin/user/member")
@Power("管理会员")
public class MemberAction extends DwzAdminAction<User>{
	private static final long serialVersionUID = 7839375284635130117L;

	@Action("add")
	@Power("添加会员")
	public String add(){
		getReferenceDomainKeys();
		return steed_forward;
	}
	private void getReferenceDomainKeys() {
		setRequestAttribute("roleNameList", DaoUtil.listAllObj(Role.class));
	}
	@Action("index")
//	@Power("查看会员列表")
	public String index(){
//		setRequestAttribute("roleNameList", BaseDao.listAllObjKey(Role.class));
		getReferenceDomainKeys();
		return super.index();
	}
	@Action("delete")
	@Power("删除会员")
	public String delete(){
		return super.delete();
	}
	@Action(value="update")
	@Power("编辑会员")
	public String update(){
		domain.aesDecodePassword();
		return super.updateNotNullField();
	}
	@Action(value="save")
	@Power("添加会员")
	public String save(){
		getRequestDomain();
		domain.aesDecodePassword();
		return super.save();
	}
	
	@Action("edit")
	@Power("编辑会员")
	public String edit(){
		User tempMember = DaoUtil.get(User.class, domain.getNickName());
		setDomain2Request(tempMember);
		getReferenceDomainKeys();
		return steed_forward;
	}
	@Action("lookOver")
	@Power("查看会员信息")
	public String lookOver(){
//		People tempMember = BaseDao.get(People.class, member.getNickName());
//		setRequestAttribute("member", tempMember);
		return super.lookOver();
	}
	/*@Action("editMemberPower")
	@Power("编辑会员权限")
	public String editMemberPower(){
		return super.edit();
	}*/
/*	@Action("updateMemberPower")
	@Power("编辑会员权限")
	public String updateMemberPower(){
		People tempMember = new People(member.getNickName());
		tempMember.setPowerSet(member.getPowerSet());
		List<String> updateEvenNull = new ArrayList<String>();
		updateEvenNull.add("powerSet");
		writeUpdateMessage(DaoUtil.updateNotNullField(tempMember,updateEvenNull));
		return null;
	}*/
}
