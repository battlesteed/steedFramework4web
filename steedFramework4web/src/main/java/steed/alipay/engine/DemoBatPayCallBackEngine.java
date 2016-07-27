package steed.alipay.engine;

import steed.alipay.domain.BatPayCallBack;
import steed.alipay.domain.BatPayCallBackDetail;
import steed.util.base.BaseUtil;

public class DemoBatPayCallBackEngine implements BatPayCallBackEngine{
	@Override
	public boolean onBatPayCallBack(BatPayCallBack batPayCallBack){
		BaseUtil.outJson(batPayCallBack);
		if(batPayCallBack.getSuccessDetailList() != null){
			for (BatPayCallBackDetail temp:batPayCallBack.getSuccessDetailList()) {
				System.out.println("支付成功的流水号--->"+temp.getFlowNumber());
			}
		}
		return true;
	}
}
