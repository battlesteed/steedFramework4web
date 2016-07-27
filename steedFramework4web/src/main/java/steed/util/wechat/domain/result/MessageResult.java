package steed.util.wechat.domain.result;

public class MessageResult extends BaseWechatResult {
	private String msg_id;
	public void setMsg_id(String msg_id) {
		this.msg_id = msg_id;
	}

	public String getMsg_id() {
		return this.msg_id;
	}
}
