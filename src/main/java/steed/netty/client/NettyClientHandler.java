package steed.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import steed.netty.module.BaseMsg;
import steed.netty.module.CommonMsg;
import steed.netty.module.MsgType;
import steed.netty.module.PingMsg;
import steed.util.base.BaseUtil;
import steed.util.base.PropertyUtil;
import steed.util.base.StringUtil;
import steed.util.system.TaskEngine;
import steed.util.system.TaskUtil;


public class NettyClientHandler extends SimpleChannelInboundHandler<BaseMsg> {
	private NettyClientBootstrap bootstrap;
    public NettyClientHandler(NettyClientBootstrap bootstrap) {
		super();
		this.bootstrap = bootstrap;
	}
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case WRITER_IDLE:
                    PingMsg pingMsg=new PingMsg();
                    ctx.writeAndFlush(pingMsg);
                    System.out.println("send ping to server----------");
                    break;
                default:
                    break;
            }
        }
    }
    @Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
       // final EventLoop eventLoop = ctx.channel().eventLoop();    
//        eventLoop.schedule(, 1L, TimeUnit.SECONDS);  
    	  new TaskEngine() {
          	@Override    
              public void doTask() {    
          		try {
          			BaseUtil.getLogger().info("链接断开,尝试重连!");
                	bootstrap.reConnect();
  	  			} catch (Exception e) {
  	  				e.printStackTrace();
  	  				BaseUtil.getLogger().info("重连失败!");
  	  				start();
  	  			}
              }    
  			
  			@Override
  			protected void startUp(ScheduledExecutorService scheduledExecutorService) {
  				scheduledExecutorService.schedule(this, 10, TimeUnit.SECONDS);
  			}
  			
  		}.start();
  		
		super.channelInactive(ctx);
	}
	@Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, BaseMsg baseMsg) throws Exception {
        MsgType msgType=baseMsg.getType();
        switch (msgType){
            case LOGIN:{
                //向服务器发起登录
            	dealMessageByEngine("login",baseMsg);
            }
            break;
            case PING:{
                System.out.println("receive ping from server----------");
            }
            break;
            case common:{
            	 //收到客户端的请求
            	BaseUtil.getLogger().debug("收到服务端消息----->"+baseMsg.getContent());
				dealMessageByEngine("common."+((CommonMsg)baseMsg).getMsgCode(),baseMsg);
            }
            break;
            default:break;
        }
        ReferenceCountUtil.release(msgType);
    }
	
	   private static NettyEngine getNettyEngine(String key){
			String className = PropertyUtil.getProperties("nettyEngine.properties").getProperty("netty.client."+key);
			if (StringUtil.isStringEmpty(className)) {
				return null;
			}
			try {
				return (NettyEngine) Class.forName(className).newInstance();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			return null;
		}

	    private void dealMessageByEngine(String key,BaseMsg baseMsg){
	    	NettyEngine nettyEngine = getNettyEngine(key);
			if (nettyEngine!=null) {
				nettyEngine.dealMessage(baseMsg,bootstrap);
			}
	    }
}
