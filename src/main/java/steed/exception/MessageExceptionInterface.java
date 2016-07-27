package steed.exception;

import steed.domain.application.Message;

public interface MessageExceptionInterface {
	public Message getMsg();
	public void setMsg(Message msg);
}
