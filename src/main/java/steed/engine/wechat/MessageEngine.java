package steed.engine.wechat;

import steed.util.wechat.domain.sys.MessageReceive;

public interface MessageEngine {
	public String getMessage(MessageReceive messageReceive);
}
