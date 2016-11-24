package steed.ext.action.admin.wechat;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;

import steed.action.annotation.Power;
import steed.domain.application.DWZMessage;
import steed.ext.action.admin.DwzAdminAction;
import steed.util.base.ContextUtil;
import steed.util.base.IOUtil;
import steed.util.base.PropertyUtil;
import steed.util.base.StringUtil;
import steed.util.digest.AESUtil;
import steed.util.wechat.MutiAccountSupportUtil;
@ParentPackage(value="steed")
@InterceptorRefs({@InterceptorRef(value="mydefault")})
@Namespace("/admin/wechat/config")
@Power("微信参数管理")
public class ConfigAction extends DwzAdminAction{
	private static final long serialVersionUID = 2917184309474601008L;
	private File certFile;
	
	public File getCertFile() {
		return certFile;
	}

	public void setCertFile(File certFile) {
		this.certFile = certFile;
	}

	@Action("index")
	@Power("微信参数设置")
	public String index(){
		return steed_forward;
	}
	@Action(value="update")
	@Power("微信参数设置")
	public String update(){
		//TODO 提供多账号支持
		HttpServletRequest request = ContextUtil.getRequest();
		Map<String, String> map = new HashMap<String, String>();
		for (String str:PropertyUtil.getAllKey("wechat")) {
			String parameter = request.getParameter(str);
			if (!StringUtil.isStringEmpty(parameter)) {
				if ("wechat.appSecret".equals(str)
						|| "wechat.token".equals(str)
						|| "wechat.encodingAESKey".equals(str)
						|| "wechat.merchant.key".equals(str)) {
					parameter = AESUtil.aesEncode(parameter);
				}
				map.put(str, parameter);
			}
		}
			//setMenus();
		PropertyUtil.putAllConfig(map);
		PropertyUtil.saveConfig();
		
		if (certFile != null) {
			try {
				IOUtil.copyFile(certFile, MutiAccountSupportUtil.getWechatAccount().getMerchantCertPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		writeObjectMessage(new DWZMessage(DWZMessage.type_update));
		return null;
	}
	
}
