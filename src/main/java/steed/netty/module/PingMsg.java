package steed.netty.module;

/**
 * Created by yaozb on 15-4-11.
 * 心跳检测的消息类型
 */
public class PingMsg extends BaseMsg {
	private static final long serialVersionUID = 4314910546601238812L;

	public PingMsg() {
        super();
        setType(MsgType.PING);
    }
}
