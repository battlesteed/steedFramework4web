package steed.ext.engine.wechat;

import steed.util.wechat.MessageUtil;
import steed.util.wechat.domain.sys.PayCallBack;
import steed.util.wechat.domain.sys.ScanPayCallBackResult;
/**
 * 支付事件成功回调回复引擎
 * @author 战马
 */
public class SimpleScanPayCallBackEngine implements ScanPayCallBackEngine{
	public String getMessage(PayCallBack scanPayCallBack) {
		//TODO 在这里写充值成功逻辑,以下是例子
		//一定要同步,微信一个订单会通知多次
		synchronized (scanPayCallBack.getOut_trade_no()) {
			 ScanPayCallBackResult result = new ScanPayCallBackResult();
			 result.setReturn_code("SUCCESS");
			 result.setReturn_msg("OK");
			 //这里模拟订单操作失败,要求微信微信再次通知订单支付成功
			 if (true){
				result.setReturn_code("FAIL");
				result.setReturn_msg("系统繁忙");
			 }
			 return MessageUtil.toXml(result);
		}
	}
	
}
