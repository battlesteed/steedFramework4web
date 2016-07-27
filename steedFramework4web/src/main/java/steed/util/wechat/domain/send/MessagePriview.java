package steed.util.wechat.domain.send;


/**
 * 消息预览
 * @author 战马
 *
 */
public class MessagePriview extends BaseMessageSend {
	private String towxname;
	private String touser;

	public void setTowxname(String towxname) {
		this.towxname = towxname;
	}

	public String getTowxname() {
		return this.towxname;
	}

	

	/*public Map<String, String> getMpnews() {
		return mpnews;
	}

	public void setMpnews(Map<String, String> mpnews) {
		this.mpnews = mpnews;
	}*/

	public String getTouser() {
		return touser;
	}
	public void setTouser(String touser) {
		this.touser = touser;
	}
}
