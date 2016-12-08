package steed.ext.action.admin.information;

import java.util.Date;

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
import steed.ext.domain.user.User;
import steed.hibernatemaster.util.DaoUtil;
import steed.util.base.StringUtil;
@Namespace("/admin/information/information")
@ParentPackage(value="steed")
@InterceptorRefs({@InterceptorRef(value="mydefault")})
@Power("管理文章")
public class InformationAction extends DwzAdminAction<Information>{
	private static final long serialVersionUID = 2127023869109798260L;
	
	/**
	 * 栏目管理
	 * @return
	 */
	@Action("index")
//	@Power("查看文章列表")
	public String index(){
		/*Page<Information> listObj = DaoUtil.listObj(pageSize,currentPage, information,null,null);
		for(Information i:listObj.getDomainCollection()){
			i.initialize();
			i.getAuthor().initialize();
			BaseUtil.outJson(i);
		}
		setRequestPage(listObj);*/
		listPrograma();
		return super.index();
	}
	@Action("add")
	@Power("添加文章")
	public String add(){
		dealUeditorLoadIssu();
		
		listPrograma();
		return steed_forward;
	}
	public void listPrograma() {
		setRequestAttribute("programaList", DaoUtil.listAllObjKey(Programa.class));
	}
	/**
	 * 解决ueditor第二次刷新不出来的问题
	 */
	private void dealUeditorLoadIssu() {
		setRequestAttribute("ueditorID", new Date().getTime());
	}
	@Action("save")
	@Power("添加文章")
	public String save(){
		Programa programa = domain.getPrograma();
		
		if (programa == null || StringUtil.isStringEmpty(programa.getName())) {
			DWZMessage dwzMessage = new DWZMessage(DWZMessage.type_save, false);
			dwzMessage.setMessage("请选择栏目");
			writeObjectMessage(dwzMessage);
			return null;
		}
		domain.setAuthor((User) getLoginUser());
		domain.setPublishDate(new Date());
		return super.save();
	}
	
	@Action("delete")
	@Power("删除文章")
	public String delete(){
		return super.delete();
	}
	@Action("edit")
	@Power("编辑文章")
	public String edit(){
		dealUeditorLoadIssu();
		listPrograma();
		super.edit();
		((Information)getRequestDomain()).base64EncodeContent();;
		return steed_forward;
	}
	@Action("update")
	@Power("编辑文章")
	public String update(){
		return super.updateNotNullField();
	}
	
	@Action("lookOver")
	@Power("查看文章")
	public String lookOver(){
		super.lookOver();
		((Information)getRequestDomain()).base64EncodeContent();
		return steed_forward;
	}
	
}
