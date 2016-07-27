package steed.alipay.engine;

import steed.alipay.domain.BatPayCallBack;

public interface BatPayCallBackEngine {
	/**
	 * 
	 * @param batPayCallBack
	 * @return 是否成功,如果不成功 ,支付宝会在24小时内按一定的时间策略重发通知
	 */
	public boolean onBatPayCallBack(BatPayCallBack batPayCallBack);
}
