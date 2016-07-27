package steed.netty.client;

import steed.netty.module.BaseMsg;

public interface NettyEngine {
	public void dealMessage(BaseMsg baseMsg,NettyClientBootstrap bootstrap);
}
