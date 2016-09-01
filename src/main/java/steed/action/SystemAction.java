package steed.action;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;

import steed.domain.wechat.PageAccessToken;
import steed.domain.wechat.WechatUser;
import steed.util.base.ContextUtil;
import steed.util.base.StringUtil;
import steed.util.dao.DaoUtil;
import steed.util.document.QRCodeUtil;
import steed.util.system.DataCacheUtil;
import steed.util.wechat.MutiAccountSupportUtil;
import steed.util.wechat.WechatInterfaceInvokeUtil;

import com.google.zxing.WriterException;
/**
 * 
 * @author 战马
 *
 * @email battle_steed@163.com
 */
@Namespace("/system")
@ParentPackage(value="steed")
@InterceptorRefs({@InterceptorRef(value="mydefault")})
public class SystemAction extends BaseAction{
	private String content;
	private int width = 100;
	private int height = 100;
	private boolean isQrcode = true;
	
	private static final long serialVersionUID = 4395374259028028362L;
	/**
	 * 二维码，条形码
	 * @return
	 * @throws WriterException
	 * @throws IOException
	 */
	@Action("qrcode")
	public String qrcode() throws WriterException, IOException{
		HttpServletResponse response = getResponse();
		response.setContentType("image/jpeg");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        QRCodeUtil.getQrCode(response.getOutputStream(), content, width, height, "jpg",isQrcode);
		return null;
	}
	
	/**
	 * 微信网页授权获取用户信息
	 * @return
	 * @throws IOException
	 */
	@Action("getWechatUserInformation")
	public String wechatUserInformation() throws IOException{
		@SuppressWarnings("unchecked")
		Map<String, Object> data = (Map<String, Object>) DataCacheUtil.getData(getRequestParameter("state"), "wechatSNSSate");
		String requestParameter = getRequestParameter("code");
		if (!StringUtil.isStringEmpty(requestParameter)) {
			MutiAccountSupportUtil.setWechatConfig(MutiAccountSupportUtil.getWechatConfig((String) data.get("appID")));
			WechatUser pullUserInformation;
			if ((Boolean)data.get("getAllInformation")) {
				pullUserInformation = WechatInterfaceInvokeUtil.pullUserInformation(requestParameter);
				if (pullUserInformation.isSuccess()) {
					if (DaoUtil.smartGet(pullUserInformation) == null) {
						pullUserInformation.save();
						if (!DaoUtil.managTransaction()) {
							pullUserInformation.setNickname("????");
							pullUserInformation.save();
						}
					}else {
						pullUserInformation.updateNotNullField(null);
					}
				}
			}else {
				pullUserInformation = new WechatUser();
			    PageAccessToken pageAccessToken = WechatInterfaceInvokeUtil.getPageAccessToken(requestParameter);
			    pullUserInformation.setOpenid(pageAccessToken.getOpenid());
			}
			
			if (pullUserInformation.isSuccess() && (Boolean) data.get("login")) {
				WechatUser temp = DaoUtil.smartGet(pullUserInformation);
				if (temp == null) {
					temp = pullUserInformation;
				}
				ContextUtil.getSession().setAttribute("wechatUser",temp);
			}
		}
		getResponse().sendRedirect((String) data.get("goUrl"));
		return null;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public boolean isQrcode() {
		return isQrcode;
	}
	public void setQrcode(boolean isQrcode) {
		this.isQrcode = isQrcode;
	}
	
	
}
