package steed.engine.task;

import java.util.List;

import org.junit.Test;

import steed.domain.system.Property;
import steed.engine.wechat.SimpleScanPayCallBackEngine;
import steed.util.base.BaseUtil;
import steed.util.dao.DaoUtil;
import steed.util.reflect.ReflectUtil;
import steed.util.system.SimpleTaskEngine;
import steed.util.wechat.MessageUtil;
import steed.util.wechat.MutiAccountSupportUtil;
import steed.util.wechat.WechatInterfaceInvokeUtil;
import steed.util.wechat.domain.result.OrderQueryResult;
import steed.util.wechat.domain.send.OrderQuerySend;
import steed.util.wechat.domain.sys.PayCallBack;
import steed.util.wechat.domain.sys.ScanPayCallBackResult;

public class OrderRefreshEngine extends SimpleTaskEngine{

	@Override
	public void doTask() {
		Property property = new Property();
		property.setPropertyType("wechatOrder");
		List<Property> listAllObj = DaoUtil.listAllObj(property);
		for(Property p:listAllObj){
			MutiAccountSupportUtil.setWechatAccount(MutiAccountSupportUtil.getWechatAccount(p.getValue()));
			OrderQuerySend send = new OrderQuerySend();
			send.setOut_trade_no(p.getKee());
			OrderQueryResult queryOrder = WechatInterfaceInvokeUtil.queryOrder(send);
			if (queryOrder.isSuccess()) {
				if ("SUCCESS".equals(queryOrder.getTrade_state())) {
					BaseUtil.getLogger().debug("查询到订单{}已经支付成功,开始做支付操作..");
					PayCallBack payCallBack = new PayCallBack();
					ReflectUtil.copySameField(payCallBack, queryOrder);
					String message = new SimpleScanPayCallBackEngine().getMessage(payCallBack);
					ScanPayCallBackResult result = MessageUtil.fromXml(message, ScanPayCallBackResult.class);
					if ("SUCCESS".equals(result.getReturn_code())) {
						p.delete();
					}
				}
			}
		}
	}
}
