package steed.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.ReferenceCountUtil;
import steed.netty.module.BaseMsg;
import steed.netty.module.CommonMsg;
import steed.netty.module.LoginMsg;
import steed.netty.module.MsgType;
import steed.netty.module.PingMsg;
import steed.util.base.BaseUtil;
import steed.util.base.PropertyUtil;
import steed.util.base.StringUtil;

/**
 * Created by yaozb on 15-4-11.
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<BaseMsg> {
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NettyChannelMap.remove((SocketChannel)ctx.channel());
    }
    
    @Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		LoginMsg loginMsg = new LoginMsg();
		ctx.writeAndFlush(loginMsg);
	}

	private static NettyEngine getNettyEngine(String key){
		String className = PropertyUtil.getProperties("nettyEngine.properties").getProperty("netty.server."+key);
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
    
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, BaseMsg baseMsg) throws Exception {
        if(MsgType.LOGIN.equals(baseMsg.getType())){
            LoginMsg loginMsg=(LoginMsg)baseMsg;
            if(PropertyUtil.getConfig("netty.userName").equals(loginMsg.getUserName())&&PropertyUtil.getConfig("netty.password").equals(loginMsg.getPassword())){
                //登录成功,把channel存到服务端的map中
                NettyChannelMap.add(loginMsg.getClientId(),(SocketChannel)channelHandlerContext.channel());
                dealMessageByEngine("login",baseMsg);
                System.out.println("client"+loginMsg.getClientId()+" 登录成功");
            }
        }else{
            if(NettyChannelMap.get(baseMsg.getClientId())==null){
                    //说明未登录，或者连接断了，服务器向客户端发起登录请求，让客户端重新登录
                    LoginMsg loginMsg=new LoginMsg();
                    channelHandlerContext.channel().writeAndFlush(loginMsg);
            }else {
            	 switch (baseMsg.getType()){
                 case PING:{
                	 BaseUtil.out("ping---------");
                     PingMsg pingMsg=(PingMsg)baseMsg;
                     PingMsg replyPing=new PingMsg();
                     NettyChannelMap.get(pingMsg.getClientId()).writeAndFlush(replyPing);
                 }
                 break;
                 
                 case common:{
                     //收到客户端的请求
                	 BaseUtil.getLogger().debug("收到客户端{}消息----->{}",baseMsg.getClientId(),baseMsg.getContent());
					dealMessageByEngine("common."+((CommonMsg)baseMsg).getMsgCode(),baseMsg);
                 }
                 break;
                 
                 default:
                 break;
             }
			}
        }
        ReferenceCountUtil.release(baseMsg);
    }

    private void dealMessageByEngine(String key,BaseMsg baseMsg){
    	NettyEngine nettyEngine = getNettyEngine(key);
		if (nettyEngine!=null) {
			nettyEngine.dealMessage(baseMsg);
		}
    }
    
	/*@Override
	protected void channelRead0(ChannelHandlerContext arg0, BaseMsg arg1)
			throws Exception {
		// TODO Auto-generated method stub
		
	}*/
}
