package steed.ext.engine.wechat;

import steed.domain.wechat.resp.TextMessage;
import steed.engine.wechat.MessageEngine;
import steed.util.wechat.MessageUtil;
import steed.util.wechat.domain.sys.MessageReceive;
/**
 * 微信小店订单支付成功回调引擎
 * @author 战马
 *
 */
public class OrderPayedCallBackEngine implements MessageEngine {

	@Override
	public String getMessage(MessageReceive messageReceive) {
		TextMessage textMessage = new TextMessage();
		MessageUtil.fitMessage(messageReceive, textMessage);
		textMessage.setContent("支付成功！请填写推荐人");
		return MessageUtil.textMessageToXml(textMessage);
	}
	

}
