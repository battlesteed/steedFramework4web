package steed.util.wechat.domain.send;

import java.util.HashMap;
import java.util.Map;

import steed.util.wechat.WechatConstantParamter;

public class BaseMessageSend {

//	private Map<String, Object> messageType;
	private String msgtype;
	private Map<String, String> mpnews;
	private Map<String, String> voice;
	public Map<String, String> getMpnews() {
		return mpnews;
	}

	public Map<String, String> getVoice() {
		return voice;
	}

	public void setVoice(Map<String, String> voice) {
		this.voice = voice;
	}

	public void setMpnews(Map<String, String> mpnews) {
		this.mpnews = mpnews;
	}
	public BaseMessageSend() {
		super();
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	/*public Map<String, Object> getMessageType() {
		return messageType;
	}

	public void setMessageType(Map<String, Object> messageType) {
		this.messageType = messageType;
	}*/

	/**
	 * 设置消息内容
	 * @param content
	 */
	public void setContent(String content) {
	
		Map<String, String> map = new HashMap<String, String>();
		map.put("media_id", content);
		if (WechatConstantParamter.messageType.msgtype_news.equals(msgtype)) {
			mpnews = map;
		}else if (WechatConstantParamter.messageType.msgtype_voice.equals(msgtype)) {
			voice = map;
		}
		//TODO 其他情况（语言，视频等）待完善
	}

	public String getMsgtype() {
		return this.msgtype;
	}

}