package steed.netty.module;

import java.io.Serializable;

/**
 */

public class CommonMsg extends BaseMsg {
	private static final long serialVersionUID = -2626489004616087312L;
	private int msgCode;

	public int getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(int msgCode) {
		this.msgCode = msgCode;
	}

	public CommonMsg() {
		super();
		setType(MsgType.common);
	}

	public CommonMsg(Serializable content) {
		this();
		setContent(content);
	}

	public CommonMsg(Serializable content, int msgCode) {
		this();
		setContent(content);
		setMsgCode(msgCode);
	}

	public CommonMsg(int msgCode) {
		this();
		setMsgCode(msgCode);
	}
}
