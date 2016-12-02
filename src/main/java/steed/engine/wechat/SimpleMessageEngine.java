package steed.engine.wechat;

import steed.util.wechat.MessageUtil;
import steed.util.wechat.domain.resp.BaseMessage;
import steed.util.wechat.domain.sys.MessageReceive;

public abstract class SimpleMessageEngine implements MessageEngine{

	@Override
	public String getMessage(MessageReceive messageReceive) {
		BaseMessage onMessageIn = onMessageIn(messageReceive);
		MessageUtil.fitMessage(messageReceive, onMessageIn);
		return MessageUtil.toXml(onMessageIn);
	}
	
	/**
	 * 当用户发送消息给公众号事会回调该方法
	 * @return 你要回复给用户的消息
	 * @param messageReceive 用户发给你的消息
	 */
	protected abstract BaseMessage onMessageIn(MessageReceive messageReceive);

}
