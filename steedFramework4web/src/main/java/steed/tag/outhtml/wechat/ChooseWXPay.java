package steed.tag.outhtml.wechat;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import steed.util.digest.Md5Util;
import steed.util.wechat.MutiAccountSupportUtil;
import steed.util.wechat.SignUtil;
/**
 * 作用自己意会
 * @author 战马
 *
 */
public class ChooseWXPay extends TagSupport{
	private static final long serialVersionUID = -5584256537704686928L;
	private String prepay_id;
	private String signType = "MD5";
	private String fail;
	private String success;
	public void setPrepay_id(String prepay_id) {
		this.prepay_id = prepay_id;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public void setFail(String fail) {
		this.fail = fail;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	@Override
	public int doStartTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("appId", MutiAccountSupportUtil.getWechatConfig().getAppID());
			Long value = (Long)(new Date().getTime()/1000);
			map.put("timeStamp", value);
			String nonceStr = Md5Util.Md5Digest(value+"steedFramwork");
			map.put("nonceStr", nonceStr);
			String package2 = "prepay_id="+prepay_id;
			map.put("package", package2);
			map.put("signType", signType);
			String paySign = SignUtil.signMap(map, signType,true).toUpperCase();
			
			out.write(" wx.chooseWXPay({\n");
			out.write("timestamp: ");
			out.write(value+",\n");
			out.write("nonceStr: '");
			out.write(nonceStr+"',\n");
			out.write("package: '");
			out.write(package2+"',\n");
			out.write("signType: '");
			out.write(signType+"',\n");
			out.write("paySign: '");
			out.write(paySign+"'");
			if (success != null) {
				out.write(",\n");
				out.write("success: ");
				out.write(success);
			}
			if (fail != null) {
				out.write(",\n");
				out.write("fail: ");
				out.write(fail);
			}
			
			out.write("});\n");
			
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
