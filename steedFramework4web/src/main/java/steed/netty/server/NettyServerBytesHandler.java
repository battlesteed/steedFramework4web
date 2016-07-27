package steed.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import steed.util.base.BaseUtil;
import steed.util.base.PropertyUtil;
import steed.util.base.StringUtil;

/**
 * Created by yaozb on 15-4-11.
 */
public class NettyServerBytesHandler extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NettyChannelMap.remove((SocketChannel)ctx.channel());
    }
    
    @Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		BaseUtil.out("客户端上线.....");
		//ctx.channel().writeAndFlush("ddd");
	//	NettyChannelMap.add("dddd", (SocketChannel)ctx.channel());
	}

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf  byteBuf) throws Exception {
    	byte[] msgByte = new byte[byteBuf.readableBytes()];
    	byteBuf.readBytes(msgByte);
    	onMessageRead(msgByte);
    }
    
    protected void onMessageRead( byte[] msgByte){
    	BaseUtil.out("服务器收到的数据-->"+new String(msgByte));
    }
    
	/*@Override
	protected void channelRead0(ChannelHandlerContext arg0, BaseMsg arg1)
			throws Exception {
		// TODO Auto-generated method stub
		
	}*/
}
