package steed.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.io.UnsupportedEncodingException;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import steed.netty.module.CommonMsg;
import steed.util.base.PropertyUtil;

/**
 * Created by yaozb on 15-4-11.
 */
public class NettyServerBootstrap {
    private int port;
    private SocketChannel socketChannel;
    public NettyServerBootstrap(int port) throws InterruptedException {
        this.port = port;
        bind();
    }

    private void bind() throws InterruptedException {
        EventLoopGroup boss=new NioEventLoopGroup();
        EventLoopGroup worker=new NioEventLoopGroup();
        ServerBootstrap bootstrap=new ServerBootstrap();
        bootstrap.group(boss,worker);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.option(ChannelOption.SO_BACKLOG, 128);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline p = socketChannel.pipeline();
                byte[] ETX = {0x0D};
                p.addFirst(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE,Unpooled.copiedBuffer(ETX)));  
//                p.addFirst(new LengthFieldBasedFrameDecoder(100000000,5,2,7,0));  
                p.addLast(new ObjectEncoder());
//                p.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
                p.addLast(new NettyServerBytesHandler());
//                p.addLast(new NettyServerHandler());
            }
        });
        ChannelFuture f= bootstrap.bind(port).sync();
        if(f.isSuccess()){
            System.out.println("server start---------------");
        }
    }
    
    public static ChannelFuture send(CommonMsg askMsg,String clientId){
    	SocketChannel channel=(SocketChannel)NettyChannelMap.get(clientId);
        if(channel!=null){
            return channel.writeAndFlush(askMsg);
        }
        return null;
    }
    public static void main(String []args) throws InterruptedException {
        NettyServerBootstrap bootstrap = new NettyServerBootstrap(PropertyUtil.getInteger("netty.serverPort"));
        CommonMsg commonMsg = new CommonMsg(121);
        commonMsg.setContent("dddddd");
        byte[] bytes = {0x3A,(byte) 0xA2,0x04,0x00,0x00,02,0x00,0x44,0x0D};
        try {
			bytes = "DCZCS2545454".getBytes("GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        while (true){
        	for (Entry<String, SocketChannel> t:NettyChannelMap.getMap().entrySet()) {
        		SocketChannel channel= t.getValue();
        		if(channel!=null){
        			String temp = "DCZCS2fdsfd\n";
        			/*for(int i=0;i<36;i++){
        				temp+=temp;
        			}*/
        			channel.writeAndFlush(temp.getBytes());
        			channel.writeAndFlush(commonMsg);
        			//channel.writeAndFlush(commonMsg);
        		}
			}
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
