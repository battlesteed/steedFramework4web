package steed.ext.domain.terminal;
/**
 * 终端返回信息实体类，（json对象模型）
 * @author 战马
 *
 */
public class ClientMessage {
	private int statusCode;
	private String message;
	/**
	 * 要返回给终端的内容
	 */
	private Object content;
	
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
/*	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
*/	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getContent() {
		return content;
	}
	public void setContent(Object content) {
		this.content = content;
	}
	
}
