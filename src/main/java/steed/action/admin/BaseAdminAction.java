package steed.action.admin;

import steed.action.BaseAction;
import steed.domain.BaseUser;
import steed.domain.GlobalParam;
import steed.hibernatemaster.domain.BaseDatabaseDomain;
/**
 * 后台管理action均需继承该类
 * @author 战马
 *
 */
public abstract class BaseAdminAction<SteedDomain extends BaseDatabaseDomain> extends BaseAction<SteedDomain>{
	private static final long serialVersionUID = -8033427047682005633L;
	@Override
	public BaseUser getLoginUser() {
		return (BaseUser) getSession().getAttribute(GlobalParam.attribute.admin);
	}
}
