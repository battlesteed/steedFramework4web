package steed.netty.server;

import io.netty.channel.socket.SocketChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import steed.util.base.BaseUtil;

/**
 * Created by yaozb on 15-4-11.
 */
public class NettyChannelMap {
    private static Map<String,SocketChannel> map=new ConcurrentHashMap<String, SocketChannel>();
    public static void add(String clientId,SocketChannel socketChannel){
        map.put(clientId,socketChannel);
    }
    public static SocketChannel get(String clientId){
       return map.get(clientId);
    }
    public static Map<String, SocketChannel> getMap(){
    	return map;
    }
    public static void remove(SocketChannel socketChannel){
        for (Map.Entry entry:map.entrySet()){
            if (entry.getValue()==socketChannel){
            	BaseUtil.getLogger().debug("客户端{}{}", entry.getKey(),"断线");
                map.remove(entry.getKey());
            }
        }
    }

}
