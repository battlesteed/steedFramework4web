package steed.engine;

import java.util.List;

import steed.domain.system.Property;
import steed.util.dao.DaoUtil;
import steed.util.system.SimpleTaskEngine;
import steed.util.wechat.MutiAccountSupportUtil;
import steed.util.wechat.WechatInterfaceInvokeUtil;

public class DeleteTempTemplateEngine extends SimpleTaskEngine{

	@Override
	public void doTask() {
		Property property = new Property();
		property.setPropertyType("tempTemplateID");
		List<Property> listAllObj = DaoUtil.listAllObj(property);
		for(Property p:listAllObj){
			MutiAccountSupportUtil.setWechatAccount(MutiAccountSupportUtil.getWechatAccount(p.getValue()));
			WechatInterfaceInvokeUtil.deleteTemplateEnsureSuccess(p.getKee());
		}
	}

}
