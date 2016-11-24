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

import steed.domain.GlobalParam;
import steed.domain.wechat.WechatAccount;
import steed.exception.runtime.system.FrameworkException;
import steed.ext.engine.wechat.MessageEngineFactory;
import steed.util.base.BaseUtil;
import steed.util.base.StringUtil;
import steed.util.wechat.MessageUtil;
import steed.util.wechat.MutiAccountSupportUtil;
import steed.util.wechat.SignUtil;
import steed.util.wechat.domain.sys.MessageReceive;
import steed.util.wechat.encrt.AesException;
import steed.util.wechat.encrt.WXBizMsgCrypt;


/** 
 * 微信servlet
 * @author 战马
 * Email battle_steed@163.com
 * 
 * http://git.oschina.net/battle_steed/steedFramework4web
 */  
public class WechatServlet extends HttpServlet{
    private static final long serialVersionUID = 4440739483644821986L; 
    private static final Logger logger = LoggerFactory.getLogger(WechatServlet.class);
  
    /** 
     * 确认请求来自微信服务器 
     */  
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {  
    	supportMutiAccount(request);
        PrintWriter out = response.getWriter();  
        String echostr = request.getParameter("echostr"); 
        if (SignUtil.checkSignature(request)) {  
            out.print(echostr);  
        }  
        out.flush();
        out.close();  
        out = null;  
    }  
  
    /** 
     * 处理微信服务器发来的消息
     */  
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {  
    	String respMessage = null;
    	try {
    		//BaseUtil.out(IOUtils.toString(request.getInputStream()));
        	supportMutiAccount(request);
        	if (!SignUtil.checkSignature(request)) {
    			return;
    		}
            // 将请求、响应的编码均设置为UTF-8（防止中文乱码）  
            request.setCharacterEncoding("UTF-8");  
            response.setCharacterEncoding("UTF-8");  
            // 调用核心业务类接收消息、处理消息  
            MessageReceive messageReceive = MessageUtil.getMessageRecive(request);
            //兼容模式懒得解密
            if (isAesCrypt(request)&&StringUtil.isStringEmpty(messageReceive.getFromUserName())) {
            	try {
    	    		String format = "<xml><ToUserName><![CDATA[%1$s]]></ToUserName><Encrypt><![CDATA[%2$s]]></Encrypt></xml>";
    	    		String fromXML = String.format(format, messageReceive.getToUserName(),messageReceive.getEncrypt());
    			
    				WXBizMsgCrypt pc = getWxBizMsgCrypt();
    				String result2 = pc.decryptMsg(request.getParameter("msg_signature"), request.getParameter("timestamp"), request.getParameter("nonce"), fromXML);
    				messageReceive = MessageUtil.getMessageRecive(result2);
    			} catch (AesException e) {
    				e.printStackTrace();
    				FrameworkException frameworkException = new FrameworkException(MutiAccountSupportUtil.getWechatAccount().getAppID()
    						+"微信号EncodingAESKey参数配置出错！！");
    				logger.error("艹", frameworkException);
    				throw frameworkException;
    			}
    		}
            if (GlobalParam.config.devMode) {
            	logger.debug("接收到的内容----->"+BaseUtil.getJson(messageReceive));
    		}
            respMessage = MessageEngineFactory.getMessage(messageReceive);  
            if (!StringUtil.isStringEmpty(respMessage) && isAesCrypt(request)) {
            	respMessage = encrypt(respMessage);
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	if (respMessage == null) {
			respMessage = "";
		}
        // 响应消息  
        PrintWriter out = response.getWriter();  
        out.print(respMessage);
        out.flush();
        out.close();  
        logger.debug("回复xml--->"+respMessage);
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
		WechatAccount wechatConfig = MutiAccountSupportUtil.getWechatAccount();
		WXBizMsgCrypt pc = new WXBizMsgCrypt(wechatConfig.getToken(), wechatConfig.getEncodingAESKey(), wechatConfig.getAppID());
		return pc;
	}
    /**
     * 根据request传过来的参数智能判断当前模式，不需要用户设置兼容模式
     * @param request
     * @return
     */
    private boolean isAesCrypt(HttpServletRequest request){
    	String encrypt_type = request.getParameter("encrypt_type");
    	return "aes".equals(encrypt_type);
    }

    private void supportMutiAccount(HttpServletRequest request) {
		if (!MutiAccountSupportUtil.isSingleMode()) {
			MutiAccountSupportUtil.setWechatAccount(MutiAccountSupportUtil.getWechatAccount(request.getParameter("key")));
		}
	}  
	  
}
