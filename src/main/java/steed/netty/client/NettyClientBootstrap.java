package steed.netty.client;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import steed.util.base.BaseUtil;
import steed.util.base.PropertyUtil;
import steed.util.base.StringUtil;


public class NettyClientBootstrap {
    private int port;
    private String host;
    private SocketChannel socketChannel;
//    public static NettyClientBootstrap nettyClientBootstrap;
    private ChannelFuture future;
    private EventLoopGroup eventLoopGroup;
    public NettyClientBootstrap(int port, String host) throws InterruptedException {
        this.port = port;
        this.host = host;
        start();
    }
    
    public NettyClientBootstrap() throws InterruptedException {
    	this(PropertyUtil.getInteger("netty.serverPort"),PropertyUtil.getConfig("netty.host"));
	}

	public NettyClientBootstrap(int port, String host, EventLoopGroup group2) throws InterruptedException {
    	this.port = port;
        this.host = host;
        this.eventLoopGroup = group2;
        start();
	}
	
	protected ChannelHandler[] getHandlers(){
		ChannelHandler[] handlers = new ChannelHandler[2];
		handlers[0] = new NettyClientBytesHandler(NettyClientBootstrap.this);
		handlers[1] = new NettyClientHandler(NettyClientBootstrap.this);
		return handlers;
	}
	
	
	private void start() throws InterruptedException {
    	if (eventLoopGroup == null) {
    		eventLoopGroup=new NioEventLoopGroup();
		}
    	Bootstrap bootstrap=new Bootstrap();
//    	bootstrap.setOption("receiveBufferSizePredictorFactory", new FixedReceiveBufferSizePredictorFactory(65535));
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE,true);
        bootstrap.group(eventLoopGroup);
        bootstrap.remoteAddress(host,port);
        bootstrap.handler( new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
//                pipeline.
                //pipeline.addFirst(new LengthFieldBasedFrameDecoder(100000000,5,2,0,4));  
//                byte[] ETX = {0x0D};
//                pipeline.addFirst(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE,Unpooled.copiedBuffer(ETX)));  
				pipeline.addLast(new IdleStateHandler(30,30,20),new ObjectEncoder(),new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
                ChannelHandler[] handlers = getHandlers();
                if (handlers != null) {
                	pipeline.addLast(handlers);
				}
                
            }
        });
        
        future =bootstrap.connect(host,port).sync();
        if (future.isSuccess()) {
            socketChannel = (SocketChannel)future.channel();
            System.out.println("connect server  成功---------");
        }
        
    }
	
	public boolean disconnect(){
		return socketChannel.disconnect().isSuccess();
	}
	
	public SocketChannel getSocketChannel() {
		return socketChannel;
	}
	public void setSocketChannel(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}
	
	public ChannelFuture send(Object data){
        return socketChannel.writeAndFlush(data);
	}
	
	private NettyEngine getNettyEngine(String key){
		key = "netty.client."+key;
		String className = PropertyUtil.getProperties("nettyEngine.properties").getProperty(key);
		if (StringUtil.isStringEmpty(className)) {
			BaseUtil.getLogger().debug("找不到"+key+"的处理器");
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
	
	public void login(){
		NettyEngine nettyEngine = getNettyEngine("login");
		if (nettyEngine != null) {
			nettyEngine.dealMessage(null,this);
		}
	}
	
	public static void main(String []args) {
		try {
			NettyClientBootstrap doConnect = new NettyClientBootstrap();
			 while (true){
	        	try {
	        		doConnect.login();
	        		doConnect.send("aaaa".getBytes("GBK"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
	            TimeUnit.SECONDS.sleep(1);
		     }
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	public void reConnect() throws InterruptedException {
		start();
	}
	/*public NettyClientBootstrap reConnect() throws InterruptedException {
		return doConnect(port,host);
	}
	
	public NettyClientBootstrap doConnect(int port,String host) throws InterruptedException {
		this.port = port;
		this.host = host;
		NettyClientBootstrap nettyClientBootstrap = new NettyClientBootstrap(port,host);
		nettyClientBootstrap.login();
		return nettyClientBootstrap;
	}*/
	
}
