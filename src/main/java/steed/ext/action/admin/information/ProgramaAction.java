package steed.ext.action.admin.information;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;

import steed.action.annotation.Power;
import steed.domain.application.DWZMessage;
import steed.ext.action.admin.DwzAdminAction;
import steed.ext.domain.information.Information;
import steed.ext.domain.information.Programa;
import steed.hibernatemaster.util.DaoUtil;

@Namespace("/admin/information/programa")
@ParentPackage(value="steed")
@InterceptorRefs({@InterceptorRef(value="mydefault")})
@Power("管理栏目")
public class ProgramaAction extends DwzAdminAction<Programa>{
	private static final long serialVersionUID = 1L;
	
	@Action("index")
//	@Power("查看栏目列表")
	public String index(){
		return super.index();
	}
	@Action("add")
	@Power("添加栏目")
	public String add(){
		return steed_forward;
	}
	@Action("save")
	@Power("添加栏目")
	public String save(){
		return super.save();
	}
	
	@Action("delete")
	@Power("删除栏目")
	public String delete(){
		Information in = new Information();
		in.setPrograma(domain);
		if (!DaoUtil.isResultNull(in)) {
			DWZMessage dwzMessage = new DWZMessage(DWZMessage.type_delete_fail);
			dwzMessage.setMessage("该栏目下存在文章，不能删除！！");
			writeObjectMessage(dwzMessage);
			return null;
		}
		return super.delete();
	}
	@Action("edit")
	@Power("编辑栏目")
	public String edit(){
		return super.edit();
	}
	@Action("update")
	@Power("编辑栏目")
	public String update(){
		return super.update();
	}
}
