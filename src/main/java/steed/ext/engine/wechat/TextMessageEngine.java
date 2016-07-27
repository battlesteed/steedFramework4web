package steed.ext.engine.wechat;

import steed.util.wechat.domain.sys.MessageReceive;
/**
 * 文本消息回复引擎
 * @author 战马
 *
 */
public class TextMessageEngine implements MessageEngine {

	@Override
	public String getMessage(MessageReceive messageReceive) {
		String content = messageReceive.getContent();
		return getTextMessage(messageReceive, content);
	}
	private String getTextMessage(MessageReceive messageReceive, String content) {

		return "";
	}

}
