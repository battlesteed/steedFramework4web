package steed.action;

import steed.domain.BaseDatabaseDomain;
import steed.domain.wechat.WechatUser;
/**
 * 
 * @author 战马
 *
 * @email battle_steed@163.com
 */
public abstract class BaseWechatAction<SteedDomain extends BaseDatabaseDomain> extends BaseAction<SteedDomain> {
	private static final long serialVersionUID = -8462482132373996278L;
	
	protected WechatUser getLoginWechatUser(){
		return (WechatUser) getSession().getAttribute("wechatUser");
	}
	
	protected void refreshLoginWechatUser(){
		getSession().setAttribute("wechatUser", getLoginWechatUser().smartGet());
	}
}
