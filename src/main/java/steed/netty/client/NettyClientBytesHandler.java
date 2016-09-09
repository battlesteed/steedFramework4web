package steed.netty.client;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import steed.util.base.BaseUtil;
import steed.util.system.TaskEngine;


public class NettyClientBytesHandler extends SimpleChannelInboundHandler<byte[]> {
	private NettyClientBootstrap bootstrap;
	
	
	
    public NettyClientBytesHandler(NettyClientBootstrap bootstrap) {
		super();
		this.bootstrap = bootstrap;
	}



	@Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case WRITER_IDLE:
                	byte[] bytes = "1".getBytes();
                    ctx.writeAndFlush(bytes);
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
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, byte[] bytes) throws Exception {
        BaseUtil.out("客户端收到的字节数组-->"+bytes);
    }
	    
}
