package steed.ext.engine.wechat;

import steed.util.wechat.domain.sys.PayCallBack;
/**
 * 扫描支付事件回调回复引擎
 * @author 战马
 *
 */
public interface ScanPayCallBackEngine{
	public String getMessage(PayCallBack scanPayCallBack);
}
