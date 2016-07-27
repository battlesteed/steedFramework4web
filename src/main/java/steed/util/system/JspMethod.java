package steed.util.system;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import steed.ext.domain.system.Menu;
import steed.ext.domain.system.Path_Power;
import steed.ext.domain.user.Power;
import steed.util.base.CollectionsUtil;
import steed.util.base.DomainUtil;
import steed.util.base.StringUtil;

/**
 * 存放供jsp页面调用的方法
 * @author 战马
 *
 */
public class JspMethod {
	/**
	 * 把路径_权限关系转换成dwz树状菜单格式
	 * @param list
	 * @param checkedlist
	 * @param sb
	 * @param name
	 * @return
	 */
	public static StringBuffer traversePath_Power(List<Path_Power> list,Collection<Power> checkedlist,StringBuffer sb,String name) {
		if(list == null){
			return sb;
		}
		for (Path_Power p:list) {
			sb.append("<li><a tname=\"");
			sb.append(name);
			sb.append("\" tvalue=\"");
			sb.append(p.getPower());
			sb.append("\"");
			
			if(!CollectionsUtil.isCollectionsEmpty(checkedlist) && checkedlist.contains(DomainUtil.fillID2Domain(p.getPower(), new Power()))){
				sb.append("checked=\"");
				sb.append("true");
				sb.append("\"");
			}
			
			sb.append(" >");
				
			sb.append(p.getPower());
			sb.append("</a>");
			if(!CollectionsUtil.isListEmpty(p.getSonList())){
				sb.append("<ul>");
				traversePath_Power(p.getSonList(),checkedlist,sb,name);
				sb.append("</ul>");
			}
			sb.append("</li>");
			//</ul/></li>
		}
		return sb;
	}
	
	/**
	 * 把List<Menu>转换成dwz左侧菜单
	 * @param list
	 * @return
	 */
	public static StringBuffer outMenu(List<Menu> menuList) {
		StringBuffer sb = new StringBuffer();
		for (Menu m:menuList) {
			sb.append("<div class=\"accordionHeader\"><h2><span>Folder</span>")
				.append(m.getName()).append("</h2></div>");
			sb.append("<div class=\"accordionContent\"><ul class=\"tree treeFolder\">");
			List<Menu> sonList = m.getSonList();
			if (CollectionsUtil.isListEmpty(sonList)) {
				m.setSonList(new ArrayList<Menu>());
				sonList.add(m);
			}
			for (Menu menu:sonList) {
				appendTreeMenu(menu, sb);
			}
			sb.append("</div></ul>");
		}
		
		return sb;
	}
	
	private static StringBuffer appendTreeMenu(Menu m,StringBuffer sb){
		List<Menu> sonList = m.getSonList();
		sb.append("<li><a");
		
		if (!StringUtil.isStringEmpty(m.getUrl())) {
			String target = m.getTarget();
			if (target == null) {
				target = "navTab";
			}
			sb.append(" href=\"")
			.append(m.getUrl())
			.append("\" target=\"")
			.append(target)
			.append("\" rel=\"menu_")
			.append(m.getId()).append("\"");
		}
		
		sb.append(" >").append(m.getName());
		sb.append("</a>");
		for (Menu menu:sonList) {
			if (!CollectionsUtil.isListEmpty(sonList)) {
				sb.append("<ul>");
				appendTreeMenu(menu,sb);
				sb.append("</ul>");
			}
		}
		sb.append("</li>");
		return sb;
	}
	
	/*private static StringBuffer appendTreeMenuSon(Menu m,StringBuffer sb){
		sb.append("<ul>");
		for (Menu menu:m.getSonList()) {
			sb.append("<li><a href=\"").append(m.getUrl())
				.append("\" target=\"navTab\" rel=\"menu_")
				.append(menu.getId()).append("\">")
				.append(menu.getName()).append("</a></li>");
		}
		sb.append("</ul>");
		return sb;
	}*/
	
	
	
}
