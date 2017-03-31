package steed.engine.wechat;

import steed.util.wechat.domain.sys.PayCallBack;
/**
 * 支付事件回调回复引擎
 * @author 战马
 *
 */
public interface WechatPayCallBackEngine{
	public String getMessage(PayCallBack scanPayCallBack);
}
