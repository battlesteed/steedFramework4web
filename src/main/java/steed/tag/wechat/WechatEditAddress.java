package steed.tag.wechat;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import steed.domain.wechat.PageAccessToken;
import steed.exception.runtime.system.FrameworkException;
import steed.util.base.PathUtil;
import steed.util.base.StringUtil;
import steed.util.digest.Md5Util;
import steed.util.system.DataCacheUtil;
import steed.util.wechat.SignUtil;
import steed.util.wechat.WechatInterfaceInvokeUtil;
/**
 * 微信支付地址共享
 * @author 战马
 *
 */
public class WechatEditAddress extends TagSupport{
	private static final long serialVersionUID = -5584256537704686928L;
	private String callBack;
	private String code;
	public void setCode(String code) {
		this.code = code;
	}
	public void setCallBack(String callBack) {
		this.callBack = callBack;
	}
	@Override
	public int doStartTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();
			ServletRequest request = pageContext.getRequest();
			if (StringUtil.isStringEmpty(code)) {
				code = request.getParameter("code");
			}
			if (StringUtil.isStringEmpty(code)) {
				throw new FrameworkException("要想调用共享地址必须把code传过来！！");
			}
			
			PageAccessToken pageAccessToken = WechatInterfaceInvokeUtil.getPageAccessToken(code);
			Map<String, Object> map2 = new HashMap<String, Object>();
			map2.put("accesstoken", pageAccessToken.getAccess_token());
			String appid = (String) DataCacheUtil.getData(request.getParameter("state"), "addressShareAppId");
			map2.put("appid", appid);
			Long timeStamp = (Long)(new Date().getTime()/1000);
			map2.put("timestamp", timeStamp);
			String nonceStr = Md5Util.Md5Digest(timeStamp+"steed");
			map2.put("noncestr", nonceStr);
			map2.put("url", PathUtil.getQueryUrl((HttpServletRequest) request));
			String addrSign = SignUtil.signMap(map2, "SHA1");
			
			out.write("WeixinJSBridge.invoke('editAddress', {");
			out.write("'appId': '");
			out.write(appid);
			out.write("'\n,");
			out.write("'scope': 'jsapi_address',\n");
			out.write("'signType': 'sha1',\n");
			out.write("'addrSign': '");
			out.write(addrSign);
			out.write("'\n,");
			out.write("'timeStamp': '");
			out.write(timeStamp+"");
			out.write("'\n,");
			out.write("'nonceStr': '");
			out.write(nonceStr);
			out.write("'\n");
			out.write("},");
			out.write(callBack);
			out.write(");");
			/* 
			 * WeixinJSBridge.invoke('editAddress', {
			 *		"appId": "${map['appid']}",
					"scope": "jsapi_address",
					"signType": "sha1",
					"addrSign": "${map['addrSign']}",
					"timeStamp": "${map['timestamp']}",
					"nonceStr": "${map['noncestr']}",
					}, function (res) {
						
					}
				); */
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		return super.doEndTag();
	}
	
	
}
