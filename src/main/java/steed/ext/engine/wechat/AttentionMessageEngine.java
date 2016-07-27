package steed.ext.engine.wechat;

import java.util.Properties;

import steed.domain.wechat.resp.TextMessage;
import steed.util.base.PropertyUtil;
import steed.util.wechat.MessageUtil;
import steed.util.wechat.domain.sys.MessageReceive;

public class AttentionMessageEngine implements MessageEngine {

	/*@Override
	public String getMessage(Map<String, String> requestMap) {
		TextMessage textMessage = new TextMessage();
		
		textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
		textMessage.setFuncFlag(0);
		MessageUtil.fitMessage(requestMap, textMessage);
		
		textMessage.setContent("欢迎关注");
		return MessageUtil.textMessageToXml(textMessage);
	}*/

	@Override
	public String getMessage(MessageReceive messageReceive) {
		TextMessage textMessage = new TextMessage();
		
		textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
//		textMessage.setFuncFlag(0);
		MessageUtil.fitMessage(messageReceive, textMessage);
		textMessage.setContent(PropertyUtil.getProperties("wechatFrameworkConfig.properties").getProperty("messageEngine.resp.attention"));
		return MessageUtil.textMessageToXml(textMessage);
	}

}
