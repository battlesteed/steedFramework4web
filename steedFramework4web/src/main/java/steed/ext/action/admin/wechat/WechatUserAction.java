package steed.ext.action.admin.wechat;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;

import steed.action.annotation.Power;
import steed.domain.wechat.WechatUser;
import steed.ext.action.admin.DwzAdminAction;
@ParentPackage(value="steed")
@InterceptorRefs({@InterceptorRef(value="mydefault")})
@Namespace("/admin/wechat/wechatUser")
@Power("管理微信用户")
public class WechatUserAction extends DwzAdminAction<WechatUser>{
	private static final long serialVersionUID = 2917184309474601008L;
	
//	@Modelo
//	private WechatUser WechatUser = DomainFactory.getDomain(WechatUser.class);
	
	@Action("index")
	@Power("查看微信用户列表")
	public String index(){
		listRefrence();
		return super.index();
	}
	private void listRefrence() {
//		setRequestAttribute("WechatUserGroupList", DaoUtil.listAllObjKey(WechatUserGroup.class));
	}
	@Action("edit")
	@Power("编辑微信用户")
	public String edit(){
		listRefrence();
		return super.edit();
	}
	@Action("update")
	@Power("编辑微信用户")
	public String update(){
		/*if (BaseUtil.isObjEmpty(WechatUser.getWechatUserGroup())) {
			WechatUser.setWechatUserGroup(null);
		}*/
		List<String> updateEvenNull = new ArrayList<String>();
		updateEvenNull.add("wechatUserGroup");
		return super.updateNotNullField(updateEvenNull);
	}
}
