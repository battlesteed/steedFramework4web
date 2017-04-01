package steed.engine.wechat;


import steed.domain.system.Property;
import steed.hibernatemaster.util.DaoUtil;
import steed.util.system.DataCacheUtil;
import steed.util.system.SynchronizedKeyGenerator;
import steed.util.wechat.MessageUtil;
import steed.util.wechat.MutiAccountSupportUtil;
import steed.util.wechat.domain.sys.PayCallBack;
import steed.util.wechat.domain.sys.ScanPayCallBackResult;
/**
 * 事件成功回调回复引擎
 * @author 战马
 */
public abstract class SimpleWechatPayCallBackEngine implements WechatPayCallBackEngine{
	private String key;
	public String getMessage(PayCallBack scanPayCallBack) {
		//TODO 在这里写充值成功逻辑,以下是例子
		//一定要同步,微信一个订单会通知多次
		key = scanPayCallBack.getOut_trade_no()+"," +MutiAccountSupportUtil.getWechatAccount().getAppID();
		synchronized (SynchronizedKeyGenerator.getKey("SimpleScanPayCallBackEngine",key)) {
			Property property = new Property();
			property.setPropertyType("wechatOrder");
			property.setKee(key);
			if (!DaoUtil.isResultNull(property) && DataCacheUtil.getData(key, "SimpleWechatPayCallBackEngineLock") == null) {
				lock();
				return getReturnMessage(onPay(scanPayCallBack));
			}else {
				return "";
			}
		}
	}
	
	private String getReturnMessage(boolean isSuccess){
		ScanPayCallBackResult result = new ScanPayCallBackResult();
		 if (isSuccess){
			 result.setReturn_code("SUCCESS");
			 result.setReturn_msg("OK");
		 }else {
			 unLock();
			 result.setReturn_code("FAIL");
			 result.setReturn_msg("系统繁忙");
		 }
		 return MessageUtil.toXml(result);
	}
	
	private void lock(){
		DataCacheUtil.setData(key, "SimpleWechatPayCallBackEngineLock", 0);
	}
	
	private void unLock(){
		DataCacheUtil.setData(key, "SimpleWechatPayCallBackEngineLock", null);
	}
	
	/**
	 * 在这里做支付之后的操作
	 * @param scanPayCallBack
	 * @return 是否操作成功,若返回false,系统会再次调用该方法
	 */
	protected abstract boolean onPay(PayCallBack scanPayCallBack);
	
}
