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
import steed.netty.module.BaseMsg;
import steed.netty.module.CommonMsg;
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
    private String clientId;
    
    public NettyClientBootstrap(int port, String host,String clientID) throws InterruptedException {
        this.port = port;
        this.host = host;
        clientId = clientID;
        start();
    }
    
    public NettyClientBootstrap(String clientID) throws InterruptedException {
    	this(PropertyUtil.getInteger("netty.serverPort"),PropertyUtil.getConfig("netty.host"),clientID);
    }
    public NettyClientBootstrap() throws InterruptedException {
    	this(PropertyUtil.getInteger("netty.serverPort"),PropertyUtil.getConfig("netty.host"), StringUtil.getSecureRandomString());
	}

	/*public NettyClientBootstrap(int port, String host, EventLoopGroup group2,String clientID) throws InterruptedException {
    	this.port = port;
        this.host = host;
        this.eventLoopGroup = group2;
       
        clientId = clientID;
        start();
	}*/
	
	protected ChannelHandler[] getHandlers(){
		ChannelHandler[] handlers = new ChannelHandler[1];
//		handlers[0] = new NettyClientBytesHandler(NettyClientBootstrap.this);
		handlers[0] = new NettyClientHandler(NettyClientBootstrap.this);
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
        
      /*  ChannelFuture connect =null;
		while(!(connect = bootstrap.connect(host,port)).isSuccess()){
        	Thread.sleep(2000);
        	BaseUtil.getLogger().warn("无法连接到服务端,两秒后重试!");
        }
		while(!(future = connect.sync()).isSuccess()){
			
			Thread.sleep(2000);
		}*/
        while(future == null || !future.isSuccess()){
	        try {
	        	future = bootstrap.connect(host,port).sync();
			} catch (Exception e) {
				Thread.sleep(2000);
	        	BaseUtil.getLogger().warn("无法连接到服务端,两秒后重试!");
			}
        }
        socketChannel = (SocketChannel)future.channel();
        
        System.out.println("connect server  成功---------");
        
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
	public ChannelFuture send(BaseMsg data){
		data.setClientId(clientId);
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
	
	public String getClientId() {
		return clientId;
	}

	public static void main(String []args) {
		try {
			NettyClientBootstrap doConnect = new NettyClientBootstrap();
			CommonMsg commonMsg = new CommonMsg(121);
		    commonMsg.setContent("客户端发过来的基本消息对象类型数据");
			/*while (true){
        		doConnect.send(commonMsg);
	            TimeUnit.SECONDS.sleep(60);
		    }*/
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
