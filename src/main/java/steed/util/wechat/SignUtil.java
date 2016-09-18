package steed.util.wechat;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import steed.exception.runtime.system.FrameworkException;
import steed.util.base.StringUtil;
import steed.util.digest.Md5Util;
import steed.util.digest.Sha1Util;
import steed.util.digest.Sha256Util;
import steed.util.reflect.ReflectUtil;
import steed.util.wechat.domain.result.UnifiedOrderResult;
import steed.util.wechat.domain.send.RedPacketSend;
import steed.util.wechat.domain.send.UnifiedOrderSend;
import steed.util.wechat.domain.sys.PayCallBack;
import steed.util.wechat.domain.sys.WechatConfig;

/** 
 * 请求校验工具类 
 *  
 */  
@SuppressWarnings("deprecation")
public class SignUtil {  
    /** 
     * 验证签名 
     *  
     * @param signature 
     * @param timestamp 
     * @param nonce 
     * @return 
     */  
    public static boolean checkSignature(String signature, String timestamp, String nonce,String token) {  
        String[] arr = new String[] { token, timestamp, nonce };  
        Arrays.sort(arr);  
        StringBuilder content = new StringBuilder();  
        for (int i = 0; i < arr.length; i++) {  
            content.append(arr[i]);  
        }  
        String sha1Digest = Sha1Util.sha1Digest(content.toString());
        content = null;
		return sha1Digest.equals(signature.toLowerCase());  
    }  
    
    /**
     * 签名下单结果,供网页端支付调用
     * @param unifiedOrder
     * @return
     */
    public static Map<String, Object> signOrderResult4JsApi(UnifiedOrderResult unifiedOrder) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", MutiAccountSupportUtil.getWechatAccount().getAppID());
		Long value = (Long)(new Date().getTime()/1000);
		map.put("timeStamp", value);
		map.put("nonceStr", Md5Util.Md5Digest("sted"+value));
		map.put("package", "prepay_id="+unifiedOrder.getPrepay_id());
		map.put("signType", "MD5");
		map.put("paySign", SignUtil.signMap(map, "MD5",true).toUpperCase());
		return map;
	}
    /**
     * 签名下单结果,供app支付调用
     * @param unifiedOrder
     * @return
     */
    public static Map<String, Object> signOrderResult4App(UnifiedOrderResult unifiedOrder) {
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("appid", MutiAccountSupportUtil.getWechatAccount().getAppID());
    	map.put("partnerid", MutiAccountSupportUtil.getWechatAccount().getMerchantId());
    	map.put("package", "Sign=WXPay");
    	Long value = (Long)(new Date().getTime()/1000);
    	map.put("noncestr", Md5Util.Md5Digest("sted"+value));
    	map.put("timestamp", value);
    	map.put("prepayid", unifiedOrder.getPrepay_id());
    	map.put("sign", SignUtil.signMap(map, "MD5",true).toUpperCase());
    	return map;
    }
    
    /**
     * 对发放红包的实体类进行签名
     * @param redPacketSend
     * @return
     */
    public static String signRedPacketSend(RedPacketSend redPacketSend){
    	WechatConfig wechatConfig = MutiAccountSupportUtil.getWechatConfig();
    	redPacketSend.setNonce_str(StringUtil.getSecureRandomString());
    	redPacketSend.setWxappid(wechatConfig.getAppID());
    	Map<String, Object> field2Map = ReflectUtil.field2Map(redPacketSend);
		String string = sortAndAppendMap(field2Map);
		string+="&key="+wechatConfig.getWechatMerchant().getKey();
		String sign = Md5Util.Md5Digest(string).toUpperCase();
		redPacketSend.setSign(sign);
		return sign;
    }
    /**
     * 对扫描支付回调实体类进行签名
     * @param redPacketSend
     * @return
     */
    public static String signScanPayCallBack(PayCallBack scanPayCallBack){
    	WechatConfig wechatConfig = MutiAccountSupportUtil.getWechatConfig();
    	Map<String, Object> field2Map = ReflectUtil.field2Map(scanPayCallBack);
    	String string = sortAndAppendMap(field2Map);
    	string+="&key="+wechatConfig.getWechatMerchant().getKey();
    	String sign = Md5Util.Md5Digest(string).toUpperCase();
    	scanPayCallBack.setSign(sign);
    	return sign;
    }
    /**
     * 对发放红包的实体类进行签名
     * @param redPacketSend
     * @return
     */
    public static String signUnifiedOrderSend(UnifiedOrderSend unifiedOrderSend){
    	WechatConfig wechatConfig = MutiAccountSupportUtil.getWechatConfig();
    	unifiedOrderSend.setNonce_str(StringUtil.getSecureRandomString());
    	unifiedOrderSend.setAppid(wechatConfig.getAppID());
    	Map<String, Object> field2Map = ReflectUtil.field2Map(unifiedOrderSend);
    	String string = sortAndAppendMap(field2Map);
    	string+="&key="+wechatConfig.getWechatMerchant().getKey();
    	String sign = Md5Util.Md5Digest(string).toUpperCase();
    	unifiedOrderSend.setSign(sign);
    	return sign;
    }

    public static String signMap(Map<String, Object> map,String signType) {
    	return signMap(map, signType, false);
    }
    
	public static String signMap(Map<String, Object> map,String signType,boolean appendMurchKey) {
    	String sortAndAppendMap = sortAndAppendMap(map);
    	if (appendMurchKey) {
			sortAndAppendMap += "&key="+MutiAccountSupportUtil.getWechatConfig().getWechatMerchant().getKey();
		}
    	signType = signType.toUpperCase();
    	if ("MD5".equals(signType)) {
			return Md5Util.Md5Digest(sortAndAppendMap);
		}else if("SHA1".equals(signType)){
			return Sha1Util.sha1Digest(sortAndAppendMap);
		}else if("SHA256".equals(signType)){
			return Sha256Util.sha256Digest(sortAndAppendMap);
		}else {
			throw new FrameworkException("找不到"+signType+"加密算法！");
		}
    }
    public static String signObj(Object object,String signType,boolean appendMurchKey) {
    	return signMap(ReflectUtil.field2Map(object), signType, appendMurchKey);
    }
	public static String sortAndAppendMap(Map<String, Object> map) {
		Object[] array = map.keySet().toArray();
		Arrays.sort(array);
		StringBuffer sb = new StringBuffer();
		for (Object temp:array) {
			sb.append(temp).append("=").append(map.get(temp)).append("&");
		}
		String string = sb.substring(0, sb.length()-1).toString();
		return string;
	}
    
    /** 
     * 验证签名 
     *  
     * @return 
     */  
    public static boolean checkSignature(HttpServletRequest request) {  
    	// 微信加密签名  
        String signature = request.getParameter("signature");  
        // 时间戳  
        String timestamp = request.getParameter("timestamp");  
        // 随机数  
        String nonce = request.getParameter("nonce");  
        return SignUtil.checkSignature(signature, timestamp, nonce,MutiAccountSupportUtil.getWechatConfig().getToken());
    }  
}  
 
