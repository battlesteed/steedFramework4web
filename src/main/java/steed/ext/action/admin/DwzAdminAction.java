package steed.ext.action.admin;

import org.apache.struts2.convention.annotation.Namespace;

import steed.action.admin.BaseAdminAction;
import steed.domain.BaseDatabaseDomain;
import steed.domain.application.DWZMessage;
import steed.ext.domain.system.Menu;
import steed.util.dao.DaoUtil;
/**
 * dwz专用action
 * @author 战马
 *
 * @email battle_steed@163.com
 */
public abstract class DwzAdminAction<SteedDomain extends BaseDatabaseDomain> extends BaseAdminAction<SteedDomain>{
	private static final long serialVersionUID = -4802757961575616721L;
	/**
	 * 整合dwz前端框架分页插件的一个方法，无需理会
	 * @param pageNum
	 */
	public void setPageNum(int pageNum) {
		this.currentPage = pageNum;
	}
	/**
	 * 整合dwz前端框架分页插件的一个方法，无需理会
	 * @param numPerPage
	 */
	public void setNumPerPage(int numPerPage) {
		this.pageSize = numPerPage;
	}
	
	protected void writeUpdateMessage(boolean isSucceed){
		writeObjectMessage(getUpdateMessage(isSucceed));
	}
	protected DWZMessage getUpdateMessage(boolean isSucceed){
		return new DWZMessage(DWZMessage.type_update, isSucceed);
	}
	
	@Override
	protected Object afterDelete(){
		return new DWZMessage(DWZMessage.type_delete, DaoUtil.managTransaction());
	}
	
	private String getNamespace(){
		Namespace nameSpace = (Namespace)this.getClass().getAnnotation(Namespace.class);
		if (nameSpace == null) {
			return null;
		}
		return nameSpace.value();
	}
	
	@Override
	protected Object afterUpdate() {
		DWZMessage dwzMessage = new DWZMessage(DWZMessage.type_update, DaoUtil.managTransaction());
		refreshIndex(dwzMessage);
		return dwzMessage;
	}
	
	private Menu getMenu(String subfix){
		String path = "."+getNamespace()+subfix;
		Menu menu = new Menu();
		menu.setUrl(path);
		return DaoUtil.listOne(menu);
	}
	
	private Menu getActionMenu() {
		Menu menu = getMenu("/index.act%");
		if (menu == null) {
			menu = getMenu("/%");
		}
		return menu;
	}
	
	/**
	 * 刷新关闭当前navtab,并刷新index.act
	 * @param dwzMessage
	 * @return
	 */
	protected DWZMessage refreshIndex(DWZMessage dwzMessage) {
		Menu menu = getActionMenu();
		if (menu != null) {
			dwzMessage.setCallbackType(DWZMessage.callbackType_closeCurrent);
			dwzMessage.setNavTabId("menu_"+menu.getId());
		}
		return dwzMessage;
	}
	
	@Override
	protected Object afterSave() {
		DWZMessage dwzMessage = new DWZMessage(DWZMessage.type_save, DaoUtil.managTransaction());
		refreshIndex(dwzMessage);
		return dwzMessage;
	}
}
