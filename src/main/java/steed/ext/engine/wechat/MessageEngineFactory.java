package steed.ext.engine.wechat;

import steed.engine.wechat.MessageEngine;
import steed.util.base.PropertyUtil;
import steed.util.base.StringUtil;
import steed.util.wechat.MessageUtil;
import steed.util.wechat.domain.sys.MessageReceive;

public class MessageEngineFactory {
	public static MessageEngine getMessageEngine(MessageReceive messageReceive) {
		String msgType = messageReceive.getMsgType().toLowerCase();
		String evenType = "";
		if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
			evenType = "."+messageReceive.getEvent();
		}
		return getMessageEngine("messageEngine.msgType."+msgType+evenType.toLowerCase());
	}
	
	private static MessageEngine getMessageEngine(String key){
		String className = PropertyUtil.getProperties("wechatFrameworkConfig.properties").getProperty(key);
		if (StringUtil.isStringEmpty(className)) {
			return null;
		}
		try {
			return (MessageEngine) Class.forName(className).newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getMessage(MessageReceive messageReceive) {
		MessageEngine engine = MessageEngineFactory.getMessageEngine(messageReceive);
		if (engine != null) {
			return engine.getMessage(messageReceive);
		}
		return "";
	}
}
