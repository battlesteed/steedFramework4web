package steed.ext.action.admin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import steed.action.annotation.Power;
import steed.domain.BaseDatabaseDomain;
import steed.domain.GlobalParam;
import steed.ext.domain.system.Menu;
import steed.ext.domain.user.User;
import steed.util.base.CollectionsUtil;
import steed.util.base.StringUtil;
import steed.util.dao.DaoUtil;

@Namespace("/admin")
@ParentPackage(value="steed")
@InterceptorRefs({@InterceptorRef(value="mydefault")})
@Power("登录后台")
public class IndexAction extends DwzAdminAction{
	private static final long serialVersionUID = 4395374259028028362L;
	@Action(value="index",results={@Result(name="success",location="/WEB-INF/jsp/admin/index_menu.jsp")})
	public String index(){
		if (getSession().getAttribute("menuList") == null) {
			User user = (User) getLoginUser();
			//BaseDao.listAllObj(Menu.class)会缓存menu，不用
			List<String> asc = new ArrayList<String>();
			asc.add("order1");
			Menu t = new Menu();
			t.setShow(true);
			List<Menu> menuList = DaoUtil.listAllObj(t, null, asc);
			
			Iterator<Menu> iterator = menuList.iterator();
			
			Map<String, String> scanActionPower = (Map<String, String>)getServletContext().getAttribute("path_powerMap");
			//删除用户没有权限的菜单
			while (iterator.hasNext()) {
				Menu next = iterator.next();
				if (next.getId() < 0) {
					continue;
				}
				String url = next.getUrl();
				if (StringUtil.isStringEmpty(url)) {
					continue;
				}
				String powerName = scanActionPower.get(url.substring(1));
				if (!StringUtil.isStringEmpty(powerName) && !user.hasPower(powerName)) {
					iterator.remove();
				}
			}
			
			List<Menu> newMenuList = new ArrayList<Menu>();
			for (Menu m : menuList) {
				Integer fatherId = m.getFatherId();
				if (fatherId == 0) {
					newMenuList.add(m);
				} else {
					menuList.get(menuList.indexOf(new Menu(fatherId))).addSon(m);
				}
			}
			removeEmptyMenu(newMenuList);
			
			setSessionAttribute("menuList", newMenuList);
		}
		//是否是横向菜单
		if ("true".equals(getRequest().getParameter("landscapeMenu"))) {
			return SUCCESS;
		}
		return steed_forward;
	}
	/**
	 * 移除没有子节点且没有url值的菜单
	 * @param newMenuList
	 */
	private void removeEmptyMenu(List<Menu> newMenuList) {
		Iterator<Menu> i = newMenuList.iterator();
		while (i.hasNext()) {
			Menu temp = i.next();
			//用户就是上帝，用户自定义的即使是空也不能删
			if (temp.getId() < 0) {
				continue;
			}
			removeEmptyMenu(temp.getSonList());
			if (StringUtil.isStringEmpty(temp.getUrl()) && CollectionsUtil.isListEmpty(temp.getSonList())) {
				i.remove();
			}
		}
	}
	
	@Action(value="menu",results={@Result(name="success",location="/WEB-INF/jsp/admin/menu.jsp")})
	public String menu(){
		List<Menu> menuList = (List<Menu>) getSession().getAttribute("menuList");
		setRequestAttribute("sonMenuList", menuList.get(menuList.indexOf(new Menu(Integer.parseInt(getRequest().getParameter("fatherId"))))));
		return steed_forward;
	}
	
	/*@Action(value="main",results={@Result(name="success",location="/WEB-INF/jsp/admin/main.jsp")})
	public String main(){
		return steed_forward;
	}
	@Action(value="head",results={@Result(name="success",location="/WEB-INF/jsp/admin/head.jsp")})
	public String head(){
		return SUCCESS;
	}*/
	@Action(value="logout",results={@Result(name="success",location="/WEB-INF/jsp/admin/login.jsp")})
	public String logout(){
		setSessionAttribute(GlobalParam.attribute.admin, null);
		setSessionAttribute(GlobalParam.attribute.user, null);
		setSessionAttribute("menuList", null);
		return SUCCESS;
	}
	
	@Override
	public BaseDatabaseDomain getModel() {
		return null;
	}
}
