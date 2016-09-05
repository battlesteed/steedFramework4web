package steed.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import steed.domain.wechat.WechatAccount;
import steed.domain.wechat.WechatConfig;
import steed.engine.wechat.SimpleScanPayCallBackEngine;
import steed.ext.plugin.WechatAccount2WechatConfigPlugin;
import steed.util.base.BaseUtil;
import steed.util.base.StringUtil;
import steed.util.dao.DaoUtil;
import steed.util.wechat.MessageUtil;
import steed.util.wechat.MutiAccountSupportUtil;
import steed.util.wechat.SignUtil;
import steed.util.wechat.domain.sys.PayCallBack;
import steed.util.wechat.encrt.AesException;
import steed.util.wechat.encrt.WXBizMsgCrypt;


/** 
 * 微信servlet
 * @author 战马
 */  
public class WechatScanPayCallBackServlet extends HttpServlet{
    private static final long serialVersionUID = 4440739483644821986L; 
    private static final Logger logger = LoggerFactory.getLogger(WechatScanPayCallBackServlet.class);
  
    /** 
     * 确认请求来自微信服务器 
     */  
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {  
    	supportMutiAccount(request);
    }  
  
    /** 
     * 处理微信服务器发来的消息
     */  
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {  
    	supportMutiAccount(request);
        PayCallBack scanPayCallBack = MessageUtil.getPayCallBack(request);
        logger.debug("接收实体类--->"+BaseUtil.getJson(scanPayCallBack));
        String sign = scanPayCallBack.getSign();
        scanPayCallBack.setSign(null);
        if(!sign.equals(SignUtil.signScanPayCallBack(scanPayCallBack))){
			logger.warn("非法扫描支付回调,微信支付回调实体类"+BaseUtil.getJson(scanPayCallBack));
			return;
		}
        PrintWriter out = response.getWriter();  
        String message = new SimpleScanPayCallBackEngine().getMessage(scanPayCallBack);
		out.print(message);
        out.flush();
        out.close();  
        logger.debug("回复xml--->"+message);
    }
    
    private String encrypt(String xml){
		try {
			WXBizMsgCrypt pc = getWxBizMsgCrypt();
			return pc.encryptMsg(xml, new Date().getTime()+"", StringUtil.getSecureRandomString());
		} catch (AesException e) {
			e.printStackTrace();
		}
		return xml;
    }

	private WXBizMsgCrypt getWxBizMsgCrypt()
			throws AesException {
		WechatConfig wechatConfig = MutiAccountSupportUtil.getWechatConfig();
		WXBizMsgCrypt pc = new WXBizMsgCrypt(wechatConfig.getToken(), wechatConfig.getEncodingAESKey(), wechatConfig.getAppID());
		return pc;
	}
    

	private void supportMutiAccount(HttpServletRequest request) {
		if (!MutiAccountSupportUtil.isSingleMode()) {
			MutiAccountSupportUtil.setWechatConfig(new WechatAccount2WechatConfigPlugin().getWechatConfig(DaoUtil.get(WechatAccount.class, request.getParameter("key"))));;
		}
	}  
	  
}
