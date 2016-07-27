package steed.util.wechat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import steed.domain.GlobalParam;
import steed.domain.wechat.PageAccessToken;
import steed.domain.wechat.WechatAccount;
import steed.domain.wechat.WechatConfig;
import steed.domain.wechat.WechatMerchant;
import steed.domain.wechat.WechatUser;
import steed.exception.runtime.system.FrameworkException;
import steed.exception.runtime.wechat.AttachTypeNotSupportedException;
import steed.exception.runtime.wechat.WechatIoException;
import steed.util.base.BaseUtil;
import steed.util.base.PathUtil;
import steed.util.base.StringUtil;
import steed.util.digest.Md5Util;
import steed.util.file.FileUtil;
import steed.util.http.HttpUtil;
import steed.util.system.DataCacheUtil;
import steed.util.system.FlowUtil;
import steed.util.wechat.domain.result.AccessToken;
import steed.util.wechat.domain.result.ArticleSummaryResult;
import steed.util.wechat.domain.result.AttachUploadResult;
import steed.util.wechat.domain.result.BaseWechatResult;
import steed.util.wechat.domain.result.GetTemplateIdResult;
import steed.util.wechat.domain.result.JsapiTicket;
import steed.util.wechat.domain.result.MenuResult;
import steed.util.wechat.domain.result.MessageResult;
import steed.util.wechat.domain.result.OrderQueryResult;
import steed.util.wechat.domain.result.QrcodeResult;
import steed.util.wechat.domain.result.RedPacketResult;
import steed.util.wechat.domain.result.RefundResult;
import steed.util.wechat.domain.result.ShopOrderQueyResult;
import steed.util.wechat.domain.result.ShortcutUrlResult;
import steed.util.wechat.domain.result.TemplateMessageResult;
import steed.util.wechat.domain.result.UnifiedOrderResult;
import steed.util.wechat.domain.result.WechatResult;
import steed.util.wechat.domain.send.Article;
import steed.util.wechat.domain.send.ArticleSummarySend;
import steed.util.wechat.domain.send.Articles;
import steed.util.wechat.domain.send.GetTemplateIdSend;
import steed.util.wechat.domain.send.Menu;
import steed.util.wechat.domain.send.MessagePriview;
import steed.util.wechat.domain.send.MessageSend;
import steed.util.wechat.domain.send.OrderQuerySend;
import steed.util.wechat.domain.send.RedPacketSend;
import steed.util.wechat.domain.send.RefundSend;
import steed.util.wechat.domain.send.SetIndustrySend;
import steed.util.wechat.domain.send.TemplateMessageSend;
import steed.util.wechat.domain.send.UnifiedOrderSend;

import com.google.gson.Gson;

/**
 * 微信接口调用工具类
 * @author 战马
 *
 */
public class WechatInterfaceInvokeUtil {
	private static final Logger log = LoggerFactory.getLogger(WechatInterfaceInvokeUtil.class);
	
	
	//private static final String[] suffixAllowed = {".jpg",".MP4",".AMR",".MP3"};
	/**
	 * 把AppID，AppSecret,ACCESS_TOKEN装配到url
	 * @param url
	 * @return
	 */
	public static String fitParam2Url(String url){
		String temp = url.replaceAll("#APPID#", MutiAccountSupportUtil.getWechatConfig().getAppID());
		if (temp.contains("#ACCESS_TOKEN#")) {
			temp = temp.replaceAll("#ACCESS_TOKEN#", AccessTokenUtil.getAccessToken().getAccess_token());
		}
		return temp.replaceAll("#APPSECRET#", MutiAccountSupportUtil.getWechatConfig().getAppSecret());
	}
	
	/*******************************\测试***********************/
	
	
	/*******************************#测试***********************/
	/*******************************\接口***********************/
	
	
	/*******************************\微信小店***********************/
	
	/**
	 * 根据订单状态/创建时间获取订单详情
	 * @param status 订单状态(不带该字段-全部状态, 2-待发货, 3-已发货, 5-已完成, 8-维权中, )
	 * @param beginTime 订单创建时间起始时间(不带该字段(传null)则不按照时间做筛选)
	 * @param endTime 订单创建时间终止时间(不带该字段(传null)则不按照时间做筛选)
	 * @return
	 */
	public static ShopOrderQueyResult getOrderByFilter(int status,Long beginTime,Long endTime){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", status);
		map.put("begintime", beginTime);
		map.put("endtime", endTime);
		return invokeWechatInterface(map, ShopOrderQueyResult.class, fitParam2Url(WechatConstantParamter.getOrderByFilteUrl));
	}
	/**
	 * 设置订单发货信息
	 * order_id	订单ID
	delivery_company	物流公司ID(参考《物流公司ID》；
	当need_delivery为0时，可不填本字段；
	当need_delivery为1时，该字段不能为空；
	当need_delivery为1且is_others为1时，本字段填写其它物流公司名称)
	delivery_track_no	运单ID(
	当need_delivery为0时，可不填本字段；
	当need_delivery为1时，该字段不能为空；
	)
	need_delivery	商品是否需要物流(0-不需要，1-需要，无该字段默认为需要物流)
	is_others	是否为6.4.5表之外的其它物流公司(0-否，1-是，无该字段默认为不是其它物流公司)
	 * @param order_id
	 * @param delivery_company
	 * @param delivery_track_no
	 * @param need_delivery
	 * @param is_others
	 * @return
	 */
	public static BaseWechatResult setDelivery(String order_id,String delivery_company,String delivery_track_no,Integer need_delivery,Integer is_others){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("order_id", order_id);
		map.put("delivery_company", delivery_company);
		map.put("delivery_track_no", delivery_track_no);
		map.put("need_delivery", need_delivery);
		map.put("is_others", is_others);
		return invokeWechatInterface(map, BaseWechatResult.class, fitParam2Url(WechatConstantParamter.setDeliveryUrl));
	}
	
	/*******************************#微信小店***********************/
	
	
	/**
	 * 上传图文消息接口
	 * @param articles
	 * @return
	 */
	public static AttachUploadResult uploadNews(Articles articles){
		return BaseUtil.parseJson(HttpUtil.getRequestString(HttpUtil.http_post, fitParam2Url(WechatConstantParamter.uploadNewsUrl), null, null, BaseUtil.getJson(articles)), AttachUploadResult.class);
	}
	/**
	 * 上传图文消息接口
	 * @param articles
	 * @return
	 */
	public static AttachUploadResult uploadNews(List<Article> articles){
		Articles articles2 = new Articles();
		articles2.setArticles(articles);
		return uploadNews(articles2);
	}
	
	/*************************\模板消息******************************/
	/**
	 * 设置模板消息所属行业
	 * @param send
	 */
	public static BaseWechatResult setIndustry(SetIndustrySend send){
		return invokeWechatInterface(send, BaseWechatResult.class, fitParam2Url(WechatConstantParamter.setIndustryUrl));
	}
	/**
	 * @see #getTemplateIdt
	 * @param send
	 * @return
	 */
	@Deprecated
	public static GetTemplateIdResult getTemplateId(GetTemplateIdSend send){
		return invokeWechatInterface(send, GetTemplateIdResult.class, fitParam2Url(WechatConstantParamter.getTemplateIdUrl));
	}
	/**
	 * 获取模板ID
	 * @param template_id_short
	 * @return
	 */
	public static GetTemplateIdResult getTemplateId(String template_id_short){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("template_id_short", template_id_short);
		return invokeWechatInterface(map, GetTemplateIdResult.class, fitParam2Url(WechatConstantParamter.getTemplateIdUrl));
	}
	/**
	 * 发送模板消息
	 * @param send
	 * @return
	 */
	public static TemplateMessageResult setTemplateMessage(TemplateMessageSend send){
		return invokeWechatInterface(send, TemplateMessageResult.class, fitParam2Url(WechatConstantParamter.sendTemplateMessageUrl));
	}
	/**
	 * 发送模板消息
	 * @param send
	 * @return
	 */
	public static TemplateMessageResult setTemplateMessage(TemplateMessageSend send,String template_id_short){
		String template_id = null;
		if (template_id_short != null && send.getTemplate_id() == null) {
			template_id = getTemplateId(template_id_short).getTemplate_id();
			send.setTemplate_id(template_id);
		}
		TemplateMessageResult invokeWechatInterface = invokeWechatInterface(send, TemplateMessageResult.class, fitParam2Url(WechatConstantParamter.sendTemplateMessageUrl));
		if (template_id != null) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("template_id", template_id);
			invokeWechatInterface(map, BaseWechatResult.class, fitParam2Url(fitParam2Url(WechatConstantParamter.deleteTemplateMessageUrl)));
		}
		return invokeWechatInterface;
	}
	
	/*************************#模板消息******************************/
	/*************************\图文统计******************************/
	/**
	 * 获取图文群发每日数据接口
	 * @return
	 */
	public static ArticleSummaryResult getArticleSummary(ArticleSummarySend articleSummarySend){
		return invokeArticleSummaryInterface(articleSummarySend, WechatConstantParamter.articleSummaryUrl);
	}
	/**
	 * 获取图文群发总数据接口
	 * @return
	 */
	public static ArticleSummaryResult getArticleTotal(ArticleSummarySend messageCount){
		return invokeArticleSummaryInterface(messageCount, WechatConstantParamter.getArticleTotalUrl);
	}
	/**
	 * 获取图文统计数据接口
	 * @return
	 */
	public static ArticleSummaryResult getUserRead(ArticleSummarySend messageCount){
		return invokeArticleSummaryInterface(messageCount, WechatConstantParamter.getUserReadUrl);
	}
	/**
	 * 获取图文统计分时数据接口
	 * @return
	 */
	public static ArticleSummaryResult getUserReadHour(ArticleSummarySend messageCount){
		return invokeArticleSummaryInterface(messageCount, WechatConstantParamter.getUserReadHourUrl);
	}
	/**
	 * 获取图文分享转发数据接口
	 * @return
	 */
	public static ArticleSummaryResult getUserShare(ArticleSummarySend messageCount){
		return invokeArticleSummaryInterface(messageCount, WechatConstantParamter.getUserShareUrl);
	}
	/**
	 * 获取图文分享转发分时数据接口
	 * @return
	 */
	public static ArticleSummaryResult getUserShareHour(ArticleSummarySend messageCount){
		return invokeArticleSummaryInterface(messageCount, WechatConstantParamter.getUserShareHourUrl);
	}
	
	private static ArticleSummaryResult invokeArticleSummaryInterface(ArticleSummarySend messageCount,String url){
		return invokeWechatInterface(messageCount, ArticleSummaryResult.class, url);
	}
	/*************************#图文统计******************************/
	
	/**
	 * 设置菜单接口
	 * @param articles
	 * @return
	 */
	public static BaseWechatResult setMenu(Menu menu){
//		return invokeWechatInterface(menu, BaseWechatResult.class, fitParam2Url(WechatConstantParamter.setMenuUrl));
		return BaseUtil.parseJson(HttpUtil.getRequestString(HttpUtil.http_post, fitParam2Url(WechatConstantParamter.setMenuUrl), null, null, BaseUtil.getJson(menu)), BaseWechatResult.class);
	}
	/**
	 * 获取菜单接口
	 * @param articles
	 * @return
	 */
	public static Menu getMenu(){
		return BaseUtil.parseJson(HttpUtil.getRequestString(HttpUtil.http_get, fitParam2Url(WechatConstantParamter.getMenuUrl), null, null, null),MenuResult.class).getMenu();
	}
	
	/**
	 * 预览消息接口
	 * @param articles
	 * @return
	 */
	public static MessageResult previewMessage(MessagePriview messagePriview){
		String requestString = HttpUtil.getRequestString(HttpUtil.http_post, fitParam2Url(WechatConstantParamter.messagePreviewUrl), null, null, BaseUtil.getJson(messagePriview));
		return BaseUtil.parseJson(requestString, MessageResult.class);
	}
	
	/**
	 * 根据分组发送消息接口
	 * @param articles
	 * @return
	 */
	public static MessageResult sendMessageByGroup(MessageSend messageSend){
		String requestString = HttpUtil.getRequestString(HttpUtil.http_post, fitParam2Url(WechatConstantParamter.sendMessageByGroupPreviewUrl), null, null, BaseUtil.getJson(messageSend));
		return BaseUtil.parseJson(requestString, MessageResult.class);
	}
	/**
	 * 获取临时二维码
	 * expire_seconds 	该二维码有效时间，以秒为单位。 最大不超过2592000（即30天），此字段如果不填(传null)，则默认有效期为30天。
		scene_id 	场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）
	 * @return
	 */
	public static QrcodeResult getTempQrcode(Long expire_seconds,int scene_id){
		Map<String, Object> map = new HashMap<String, Object>();
		if (expire_seconds == null) {
			expire_seconds = 2592000L;
		} 
		map.put("expire_seconds",expire_seconds);
		Map<String, Object> action_info = new HashMap<String, Object>();
		Map<String, Object> scene = new HashMap<String, Object>();
		scene.put("scene_id", scene_id);
		action_info.put("scene", scene);
		map.put("action_info",action_info);
		map.put("action_name","QR_SCENE");
		String requestString = HttpUtil.getRequestString(HttpUtil.http_post, fitParam2Url(WechatConstantParamter.getQrcodeUrl), null, null, BaseUtil.getJson(map));
		return BaseUtil.parseJson(requestString, QrcodeResult.class);
	}
	/**
	 * 获取参数二维码scene_str == null时为临时二维码,否则是获取永久二维码
	action_name 	二维码类型，QR_SCENE为临时,QR_LIMIT_SCENE为永久,QR_LIMIT_STR_SCENE为永久的字符串参数值
	action_info 	二维码详细信息
	scene_id 	场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）
	scene_str 	场景值ID（字符串形式的ID），字符串类型，长度限制为1到64，仅永久二维码支持此字段 
	 * @return
	 */
	public static QrcodeResult getPermanentQrcode(String scene_str,Integer scene_id){
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> action_info = new HashMap<String, Object>();
		Map<String, Object> scene = new HashMap<String, Object>();
		scene.put("scene_id", scene_id);
		scene.put("scene_str", scene_str);
		action_info.put("scene", scene);
		map.put("action_info",action_info);
		if (scene_str != null) {
			map.put("action_name","QR_LIMIT_STR_SCENE");
		}else {
			map.put("action_name","QR_SCENE");
			map.put("expire_seconds",2592000);
		}
		String requestString = HttpUtil.getRequestString(HttpUtil.http_post, fitParam2Url(WechatConstantParamter.getQrcodeUrl), null, null, BaseUtil.getJson(map));
		return BaseUtil.parseJson(requestString, QrcodeResult.class);
	}
	/**
	 * 长链接转短连接
	 * @param long_url
	 * @return 短链接
	 */
	public static ShortcutUrlResult shorturl(String long_url){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("action", "long2short");
		map.put("long_url", long_url);
		return invokeWechatInterface(map, ShortcutUrlResult.class, WechatConstantParamter.getShortUrl);
	}
	
	/**
	 * 从微信服务器获取JsapiTicket,不会缓存JsapiTicket，
	 * 请调用JsapiTicketUtil
	 * @see steed.util.wechat.JsapiTicketUtil
	 */
	public static JsapiTicket getJsapiTicket(){
		String url = WechatInterfaceInvokeUtil.fitParam2Url(WechatConstantParamter.getJsapiTicketUrl);
		String json = HttpUtil.getRequestString(HttpUtil.http_get, url, null, null);
		JsapiTicket data = new Gson().fromJson(json, JsapiTicket.class);
		data.setAccess_token_getTime(new Date().getTime());
		log.debug("JsapiTicket----->"+data.getTicket());
		return data;
	}
	/**
	 * 从微信服务器获取accessToken,不会缓存accessToken，
	 * 请调用steed.util.wechat.AccessTokenUtil
	 * @see steed.util.wechat.AccessTokenUtil
	 */
	public static AccessToken getAccessToken(){
		String url = WechatInterfaceInvokeUtil.fitParam2Url(WechatConstantParamter.getAccessTokenUrl);
		String json = HttpUtil.getRequestString(HttpUtil.http_get, url, null, null);
		AccessToken data = new Gson().fromJson(json, AccessToken.class);
		data.setAccess_token_getTime(new Date().getTime());
		log.debug("accessToken----->"+data.getAccess_token());
		return data;
	}
	
	public static void getUserInformation(HttpServletResponse response, HttpServletRequest request,boolean getAllInformation,boolean login){
		try {
			response.sendRedirect(getUserSNSUrl(getAllInformation, login, PathUtil.getQueryUrl(request)));
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException(e);
		}
	}
	/**
	 * 拉取用户信息
	 * @param access_token
	 * @param openid
	 * @param lang 语言 一般传'zh_CN'
	 * @return
	 */
	public static WechatUser pullUserInformation(String access_token,String openid,String lang){
		String url = fitParam2Url(WechatConstantParamter.pullUserInformationUrl)
			.replaceFirst("#PAGE_ACCESS_TOKEN#", access_token)
			.replaceFirst("#OPENID#", openid).replaceFirst("#LANG#", lang);
		String requestString = HttpUtil.getRequestString(HttpUtil.http_get, url, null, null, null);
		log.debug("拉取到的用户信息--->"+requestString);
		return BaseUtil.parseJson(requestString, WechatUser.class);
	}
	/**
	 * 根据code拉取用户信息
	 */
	public static WechatUser pullUserInformation(String code){
		PageAccessToken pageAccessToken = getPageAccessToken(code);
		return pullUserInformation(pageAccessToken.getAccess_token(), pageAccessToken.getOpenid(), "zh_CN");
	}
	/**
	 * 用code兑换PageAccessToken
	 * @param code
	 * @return
	 */
	public static PageAccessToken getPageAccessToken(String code){
		String url = fitParam2Url(WechatConstantParamter.getAuthorAssessTokenUrl).replaceAll("#CODE#", code);
		String requestString = HttpUtil.getRequestString(HttpUtil.http_post, url, null, null, null);
		return BaseUtil.parseJson(requestString, PageAccessToken.class);
	}
	
	
	
	/**
	 * 获取用于拉取用户信息重定向的url
	 * @param getAllInformation 是否获取用户除openID外的其他信息
	 * @param goUrl 获取用户信息成功后跳转的url
	 * @return
	 */
	public static String getUserSNSUrl(boolean getAllInformation,boolean login,String goUrl){
		String fitParam2Url = fitParam2Url(WechatConstantParamter.getAuthorCodeUrl);
		String scope = "snsapi_base";
		if (getAllInformation) {
			scope = "snsapi_userinfo";
		}
		String state = new Date().getTime()+""+new Random().nextInt();
		fitParam2Url = fitParam2Url.replaceFirst("#SCOPE#", scope)
				.replaceFirst("#STATE#", state)
				.replaceFirst("#REDIRECT_URI#", StringUtil.encodeUrl(PathUtil.getBrowserPath("/system/getWechatUserInformation.act")));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("goUrl", goUrl);
		map.put("appID", MutiAccountSupportUtil.getWechatConfig().getAppID());
		map.put("login", login);
		map.put("getAllInformation", getAllInformation);
		DataCacheUtil.setData(state, "wechatSNSSate", map);
		return fitParam2Url;
	}
	
	public static void getAddressShareToken(HttpServletResponse response, HttpServletRequest request){
		try {
			response.sendRedirect(getAddressShareTokenUrl(PathUtil.getQueryUrl(request)));
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException(e);
		}
	}
	
	public static String getAddressShareTokenUrl(String redirectUri){
		String fitParam2Url = fitParam2Url(WechatConstantParamter.getAuthorCodeUrl);
		String scope = "snsapi_base";
		String state = new Date().getTime()+""+new Random().nextInt();
		fitParam2Url = fitParam2Url.replaceFirst("#SCOPE#", scope)
				.replaceFirst("#STATE#", state)
				.replaceFirst("#REDIRECT_URI#", StringUtil.encodeUrl(redirectUri));
		//Map<String, Object> map = new HashMap<String, Object>();
		//map.put("appID", MutiAccountSupportUtil.getWechatConfig().getAppID());
		DataCacheUtil.setData(state, "addressShareAppId", MutiAccountSupportUtil.getWechatConfig().getAppID());
		return fitParam2Url;
	}
	/**
	 * 下载多媒体素材
	 * @param mediaID 媒体素材ID
	 * @return 浏览器能访问到的相对路径
	 */
	public static String downLoadMidea(String mediaID){
		CloseableHttpResponse response = null;
		try {
			HttpRequestBase buildRequest = HttpUtil.buildRequest(HttpUtil.http_get, fitParam2Url(WechatConstantParamter.getMediaUrl).replaceAll("#MEDIA_ID#", mediaID), null, null);
			response = HttpClients.createDefault().execute(buildRequest);
			
			String fileName = response.getFirstHeader("Content-disposition").getValue();
			fileName = fileName.substring("attachment; filename=\"".length(), fileName.length()-1);
			
			HttpEntity entity = response.getEntity();
			byte[] bit = EntityUtils.toByteArray(entity);
			File file = new File(GlobalParam.FOLDER.rootPath+"wechatmedia\\"+fileName);
			file.mkdirs();
			file.delete();
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(bit);
			fileOutputStream.flush();
			fileOutputStream.close();
			return "/wechatmedia/"+fileName;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	
	/****************************\支付相关******************************/
	
	/**
	 * 订单查询接口
	 * @return
	 */
	public static OrderQueryResult queryOrder(OrderQuerySend orderQuerySend){
		WechatConfig wechatConfig = MutiAccountSupportUtil.getWechatConfig();
		WechatMerchant wechatMerchant = wechatConfig.getWechatMerchant();
		orderQuerySend.setMch_id(wechatMerchant.getId());
		orderQuerySend.setAppid(wechatConfig.getAppID());
		orderQuerySend.setNonce_str(Md5Util.Md5Digest(new Date().getTime()+""+new Random().nextInt()));
		orderQuerySend.setSign(null);
		orderQuerySend.setSign(SignUtil.signObj(orderQuerySend, "MD5", true).toUpperCase());
		String orderQuerySendToXml = MessageUtil.toXml(orderQuerySend);
		log.debug("查询订单接口，发送数据----->{}",new Object[]{orderQuerySendToXml});
		try {
			return MessageUtil.fromXml(HttpUtil.getRequestString(HttpUtil.http_post,WechatConstantParamter.queryOrderUrl, null, null, orderQuerySendToXml),OrderQueryResult.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 统一下单接口
	 * @return
	 */
	public static UnifiedOrderResult unifiedOrder(UnifiedOrderSend unifiedOrderSend){
		WechatConfig wechatConfig = MutiAccountSupportUtil.getWechatConfig();
		WechatMerchant wechatMerchant = wechatConfig.getWechatMerchant();
		unifiedOrderSend.setMch_id(wechatMerchant.getId());
		unifiedOrderSend.setSign(null);
		unifiedOrderSend.setAppid(wechatConfig.getAppID());
		if (unifiedOrderSend.getNotify_url() == null) {
			unifiedOrderSend.setNotify_url(PathUtil.getBrowserPath("/scanPayCallBackServlet.jsp"));
		}
		SignUtil.signUnifiedOrderSend(unifiedOrderSend);
		String redPacketSendMessageToXml = MessageUtil.unifiedOrderSendToXml(unifiedOrderSend);
		try {
			return MessageUtil.XmlToUnifiedOrderResult(HttpUtil.getRequestString(HttpUtil.http_post,WechatConstantParamter.unifiedOrderUrl, null, null, redPacketSendMessageToXml));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param body
	 * @param money
	 * @param clientIP
	 * @param TradeType
	 * @param openId
	 * 
	 * @see #getUnifiedOrderSend(String, int, String, String, String)
	 * @return
	 */
	public static UnifiedOrderResult unifiedOrder(String body, int money,String clientIP,String TradeType,String openId){
		UnifiedOrderSend orderSend = getUnifiedOrderSend(body,money,clientIP,TradeType,openId);
		return WechatInterfaceInvokeUtil.unifiedOrder(orderSend);
	}
	
	/**
	 * 对下单接口返回来的prepay_id签名,用于客户端或网页端支付用
	 * @param unifiedOrder
	 * @return
	 */
	public static Map<String, Object> signPayJsSdk(UnifiedOrderResult unifiedOrder) {
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
	 * 
	 * @param money
	 * @param clientIP
	 * @param TradeType 交易类型,取值如下：JSAPI，NATIVE，APP，WAP,详细说明见
	 * @param openId
	 * @param body 订单信息
	 * @return
	 */
	public static UnifiedOrderSend getUnifiedOrderSend(String body,int money,String clientIP,String TradeType,String openId) {
		UnifiedOrderSend orderSend = new UnifiedOrderSend();
		orderSend.setAttach("type_pay");
		orderSend.setBody(body);
		String orderKey = "steedWechatOrderNumber";
		WechatAccount wechatAccount = MutiAccountSupportUtil.getWechatAccount();
		if (wechatAccount != null) {
			orderKey = wechatAccount.getAppID() + "WechatOrderNumber";
		}
		orderSend.setOut_trade_no(FlowUtil.getFlowString(orderKey, 6, true));
		orderSend.setTotal_fee(money);
		orderSend.setNotify_url(PathUtil.getBrowserPath("/wechat/scanPayCallBackServlet.jsp"));
		orderSend.setSpbill_create_ip(clientIP);
		orderSend.setTrade_type(TradeType);
		orderSend.setOpenid(openId);
		return orderSend;
	}
	/**
	 * 退款接口
	 * @return
	 */
	public static RefundResult reFund(RefundSend refundSend){
		WechatConfig wechatConfig = MutiAccountSupportUtil.getWechatConfig();
		WechatMerchant wechatMerchant = wechatConfig.getWechatMerchant();
		refundSend.setAppid(wechatConfig.getAppID());
		refundSend.setMch_id(wechatMerchant.getId());
		refundSend.setNonce_str(Md5Util.Md5Digest(new Date().getTime()+""+new Random().nextInt()));
		refundSend.setSign(null);
		refundSend.setSign(SignUtil.signObj(refundSend, "MD5", true));
		String redPacketSendMessageToXml = MessageUtil.toXml(refundSend);
		try {
			return MessageUtil.fromXml(HttpUtil.getRequestString(fitParam2Url(WechatConstantParamter.refundtUrl), null, null, redPacketSendMessageToXml, wechatMerchant.getCertPath(), wechatMerchant.getId()),RefundResult.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 红包发放接口
	 * @return
	 */
	public static RedPacketResult sendRedPacket(RedPacketSend redPacketSend){
		WechatConfig wechatConfig = MutiAccountSupportUtil.getWechatConfig();
		WechatMerchant wechatMerchant = wechatConfig.getWechatMerchant();
		redPacketSend.setMch_id(wechatMerchant.getId());
		if (StringUtil.isStringEmpty(redPacketSend.getSign())) {
			SignUtil.signRedPacketSend(redPacketSend);
		}
		String redPacketSendMessageToXml = MessageUtil.redPacketSendMessageToXml(redPacketSend);
		try {
			return MessageUtil.XmlToRedPacketSendMessage(HttpUtil.getRequestString(fitParam2Url(WechatConstantParamter.sendRedPacketUrl), null, null, redPacketSendMessageToXml, wechatMerchant.getCertPath(), wechatMerchant.getId()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/****************************#支付相关******************************/
	/**
	 * 上传素材接口
	 * @param filePath
	 * @param isthumb 是否是缩略图
	 * @return
	 */
	public static AttachUploadResult uploadAttach(String filePath) {
		 CloseableHttpClient httpclient = HttpClients.createDefault();
	        try { 
	        	//TODO 把上传文件方法提取到httpUtil
//	        	String url = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=juezCyyRTkt_E0lWZQaUFYkQuiU6kVE2DkA9uIQjIRWte1FWy15t5rYRUdiatQciDyEdHlhCU46_S_OKhrFrU5hX3AM_yGrsIjUoWTYyoyY&type=image";
	        	String url = WechatInterfaceInvokeUtil.fitParam2Url(WechatConstantParamter.uploadAttachUrl);
	        	String attachType;
//	        	if (isthumb) {
//					attachType = "thumb";
//				}else {
					attachType = getAttachType(FileUtil.getFileSuffix(filePath));
//				}
				url = url.replaceAll("#TYPE#", attachType);
	        	
	            HttpPost httppost = new HttpPost(url); 
	            FileBody img = new FileBody(new File(filePath)); 
	            StringBody filename = new StringBody(img.getFilename(), ContentType.TEXT_PLAIN); 
	            HttpEntity reqEntity = MultipartEntityBuilder.create() 
	                    .addPart("img", img) 
	                    .addPart("filename", filename) 
	                    .build();
	            httppost.setEntity(reqEntity); 
	            CloseableHttpResponse response = httpclient.execute(httppost); 
	            try { 
	                HttpEntity resEntity = response.getEntity(); 
                    AttachUploadResult attachUploadResult = new Gson().fromJson(EntityUtils.toString(resEntity), AttachUploadResult.class);
	                EntityUtils.consume(resEntity); 
	                return attachUploadResult;
	            } finally { 
	                response.close(); 
	            } 
	        }catch (IOException e) {
				e.printStackTrace();
				throw new WechatIoException(e);
			}  finally { 
	            try {
					httpclient.close();
				} catch (IOException e) {
					e.printStackTrace();
				} 
	        } 
	}
	/*******************************#接口***********************/
	
	
	
	/***************************\私有方法********************/
	
	/**
	 * 
	 * @param objSend
	 * @param clazz
	 * @param url
	 * @return
	 */
	private static <T extends WechatResult> T invokeWechatInterface(Object objSend,Class<T> clazz,String url){
		String requestString = HttpUtil.getRequestString(HttpUtil.http_post, fitParam2Url(url), null, null, BaseUtil.getJson(objSend));
		return BaseUtil.parseJson(requestString, clazz);
	}
	
	
	
	
	private static String getAttachType(String suffix){
		String lowerCase = suffix.toLowerCase();
		//我***，为兼容jdk1.6,不能用switch
		if (".jpg".equals(lowerCase) 
				|| ".jpeg".equals(lowerCase)
				|| ".png".equals(lowerCase)
				|| ".gif".equals(lowerCase)) {
			return "image";
		}else if(".mp4".equals(lowerCase)){
			return "video";
		}else if(".mp3".equals(lowerCase)
				|| ".wma".equals(lowerCase)
				|| ".wav".equals(lowerCase)
				|| ".arm".equals(lowerCase)){
			return "voice";
		}else {
			throw new AttachTypeNotSupportedException("暂时不支持上传"+suffix+"格式的素材！！");
		}
	}
	/***************************#私有方法********************/
	
}
