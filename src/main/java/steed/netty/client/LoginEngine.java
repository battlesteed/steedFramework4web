package steed.netty.client;

import steed.netty.module.BaseMsg;
import steed.netty.module.LoginMsg;
import steed.util.base.PropertyUtil;

public class LoginEngine implements NettyEngine{
	@Override
	public void dealMessage(BaseMsg baseMsg,NettyClientBootstrap bootstrap) {
		System.out.println("登陆");
		LoginMsg loginMsg = new LoginMsg();
        loginMsg.setPassword(PropertyUtil.getConfig("netty.password"));
        loginMsg.setUserName(PropertyUtil.getConfig("netty.userName"));
        loginMsg.setContent("mycontent,你自己定义的数据,用于登陆时候做数据交换");
        bootstrap.send(loginMsg);
	}

}
