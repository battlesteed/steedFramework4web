package steed.netty.server;

import steed.netty.module.BaseMsg;

public interface NettyEngine {
	public void dealMessage(BaseMsg baseMsg);
}
