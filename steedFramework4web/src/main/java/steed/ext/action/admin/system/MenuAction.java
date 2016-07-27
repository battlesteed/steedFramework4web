package steed.ext.action.admin.system;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;

import steed.action.annotation.Power;
import steed.domain.BaseDatabaseDomain;
import steed.domain.application.DWZMessage;
import steed.ext.action.admin.DwzAdminAction;
import steed.ext.domain.system.Menu;
import steed.util.dao.DaoUtil;
import steed.util.system.ValidateUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
@ParentPackage(value="steed")
@InterceptorRefs({@InterceptorRef(value="mydefault")})
@Namespace("/admin/system/menu")
@Power("管理菜单")
/**
 * 菜单管理相关action
 * @author 战马
 */
public class MenuAction extends DwzAdminAction{
	private static final long serialVersionUID = 7839375284635130117L;

	@Action("index")
	@Power("编辑菜单")
	public String index(){
//		setRequestAttribute("menuList", BaseDao.listAllObj(Menu.class));
		List<String> asc = new ArrayList<String>();
		asc.add("order1");
		List<Menu> menuList = DaoUtil.listAllObj(new Menu(), null, asc);
		setRequestAttribute("zNodes", menus2zNodes(menuList));
		return steed_forward;
	}
	/**
	 * 把菜单转成ztree的zNodes格式
	 * @param menuList
	 * @return
	 */
	private StringBuffer menus2zNodes(List<Menu> menuList){
		StringBuffer sb = new StringBuffer();
		for (Menu m:menuList) {
			sb.append("{ id:").append(m.getId()).append(",")
			.append(" pId:").append(m.getFatherId()).append(",")
			.append(" name:\"").append(m.getName()).append("\"");
			if (m.getId() < 0) {
				sb.append(" ,url:\"").append(m.getUrl()).append("\"");
			}
//			.append(", dropRoot:false ")
			sb.append("},");
		}
		return sb;
	}
	
	@Action("update")
	@Power("编辑菜单")
	public String update(){
		List<Menu> menuList = new Gson().fromJson(getRequestParameter("menuList"), new TypeToken<List<Menu>>(){}.getType());
		try {
			ValidateUtil.validateCollection(menuList);
			List<Menu> systemMenu = new ArrayList<Menu>();
			Iterator<Menu> iterator = menuList.iterator();
			
			while(iterator.hasNext()){
				Menu next = iterator.next();
				if (next.getId() > 0) {
					systemMenu.add(next);
					iterator.remove();
				}else {
					next.setShow(true);
				}
			}
			
			//删除之前所有用户自定义的菜单
			Menu menu = new Menu();
			menu.setId_max_1(-1);
			DaoUtil.deleteByQuery(menu);
			
			int count = DaoUtil.updateListNotNullFieldOneByOne(systemMenu, null);
			
			writeUpdateMessage(DaoUtil.saveList(menuList) && count == 0);
		} catch (Exception e) {
			e.printStackTrace();
			DWZMessage message = new DWZMessage(DWZMessage.type_update_fail);
			message.setMessage("您填写的菜单含有非法数据！");
			writeObjectMessage(message);
		}
		return null;
	}
	
	@Override
	public BaseDatabaseDomain getModel() {
		return null;
	}
	
}
