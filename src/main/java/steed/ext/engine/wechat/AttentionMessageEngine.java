package steed.ext.engine.wechat;

import steed.engine.wechat.SimpleMessageEngine;
import steed.util.base.PropertyUtil;
import steed.util.wechat.domain.resp.BaseMessage;
import steed.util.wechat.domain.resp.TextMessage;
import steed.util.wechat.domain.sys.MessageReceive;

public class AttentionMessageEngine extends SimpleMessageEngine {

	@Override
	protected BaseMessage onMessageIn(MessageReceive messageReceive) {
		TextMessage textMessage = new TextMessage();
		textMessage.setContent(PropertyUtil.getProperties("wechatFrameworkConfig.properties").getProperty("messageEngine.resp.attention"));
		return textMessage;
	}

}
