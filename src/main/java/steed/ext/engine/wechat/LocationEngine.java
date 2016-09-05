package steed.ext.engine.wechat;

import steed.engine.wechat.MessageEngine;
import steed.util.system.DataCacheUtil;
import steed.util.wechat.domain.sys.MessageReceive;
/**
 * 文本消息回复引擎
 * @author 战马
 *
 */
public class LocationEngine implements MessageEngine {

	@Override
	public String getMessage(MessageReceive messageReceive) {
		DataCacheUtil.setData(messageReceive.getFromUserName(), "wechatLongitude", messageReceive.getLongitude());
		DataCacheUtil.setData(messageReceive.getFromUserName(), "wechatLatitude", messageReceive.getLatitude());
		return "";
	}
}
