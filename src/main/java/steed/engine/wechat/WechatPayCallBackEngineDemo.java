package steed.engine.wechat;

import steed.util.base.BaseUtil;
import steed.util.wechat.domain.sys.PayCallBack;

public class WechatPayCallBackEngineDemo extends SimpleWechatPayCallBackEngine{

	@Override
	protected boolean onPay(PayCallBack scanPayCallBack) {
		BaseUtil.getLogger().error("请配置wechatFrameworkConfig.properties里面的wechatPayCallBackEngine来处理微信支付成功通知消息!");
		return false;
	}

}
