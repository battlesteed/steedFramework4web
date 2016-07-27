package steed.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.concurrent.TimeUnit;

import steed.netty.module.BaseMsg;
import steed.util.base.BaseUtil;
import steed.util.base.PropertyUtil;
import steed.util.base.StringUtil;
import steed.util.system.TaskUtil;


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
        TaskUtil.startTask(new Runnable() {    
          @Override    
          public void run() {    
            try {
            	System.out.println("链接断开,尝试重连!");
            	bootstrap.reConnect();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("重连失败!");
				TaskUtil.startTask(this, 10, TimeUnit.SECONDS);
			}
          }    
        }, 10, TimeUnit.SECONDS);
		super.channelInactive(ctx);
	}
	@Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, byte[] bytes) throws Exception {
        BaseUtil.out("客户端收到的字节数组-->"+bytes);
    }
	    
}
