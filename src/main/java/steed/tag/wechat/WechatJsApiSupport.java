package steed.tag.wechat;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import steed.util.base.PathUtil;
import steed.util.base.StringUtil;
import steed.util.digest.Md5Util;
import steed.util.wechat.JsapiTicketUtil;
import steed.util.wechat.MutiAccountSupportUtil;
import steed.util.wechat.SignUtil;
/**
 * 作用自己意会
 * @author 战马
 *
 */
public class WechatJsApiSupport extends TagSupport{
	private static final long serialVersionUID = -5584256537704686928L;
	private boolean debug = false;
	private final String[] apis = {"chooseWXPay","hideOptionMenu","openLocation","scanQRCode"};
	private String apiList = "";

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setApiList(String apiList) {
		this.apiList = apiList;
	}


	@Override
	public int doStartTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();
			out.write("<script type=\"text/javascript\" src=\"http://res.wx.qq.com/open/js/jweixin-1.0.0.js\"></script>\n");
			
			Map<String, Object> map2 = new HashMap<String, Object>();
			Long value = (Long)(new Date().getTime()/1000);
			String noncestr = Md5Util.Md5Digest(value+"steed");
			map2.put("noncestr", noncestr);
			map2.put("timestamp", value);
			map2.put("jsapi_ticket", JsapiTicketUtil.getJsapiTicket().getTicket());
			map2.put("url", PathUtil.getQueryUrl((HttpServletRequest) pageContext.getRequest()).replaceFirst("/WEB-INF/jsp", "").replaceFirst("\\.jsp", ".act"));
			String signature = SignUtil.signMap(map2, "SHA1");
			out.print("<script type=\"text/javascript\">\n");
			out.write("wx.config({\n");
			out.write("debug: ");
			out.write(debug+",\n");
			out.write("appId: '");
			out.write(MutiAccountSupportUtil.getWechatAccount().getAppID()+"',\n");
			out.write("timestamp: ");
			out.write(value+",\n");
			out.write("nonceStr: '");
			out.write(noncestr+"',\n");
			out.write("signature: '");
			out.write(signature+"',\n");
			out.write("jsApiList: [");
			String[] split;
			if (StringUtil.isStringEmpty(apiList)) {
				//TODO 把所有的api写出去
				split = apis;
			}else {
				split = apiList.split(",");
			}
			for (int i=0;i<split.length;i++) {
				out.print("'");
				out.print(split[i]);
				out.print("'");
				if (i != split.length-1) {
					out.print(",");
				}
			}
			out.write("]\n");
			out.write("});\n");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		JspWriter out = pageContext.getOut();
		try {
			out.print("\n</script>");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return super.doEndTag();
	}
	
	
}
