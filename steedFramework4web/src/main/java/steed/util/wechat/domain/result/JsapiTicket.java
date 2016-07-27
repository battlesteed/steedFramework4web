package steed.util.wechat.domain.result;


public class JsapiTicket extends BaseWechatResult{
	private String ticket;
	/**
	 * 上一次获取access_token的时间
	 */
	private long access_token_getTime;
	/**
	 * 凭证有效时间，单位：秒 
	 */
	private int expires_in = 0;

	public JsapiTicket() {
	}
	
	public JsapiTicket(String ticket, long access_token_getTime,int expires_in) {
		this.ticket = ticket;
		this.access_token_getTime = access_token_getTime;
		this.expires_in = expires_in;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public long getAccess_token_getTime() {
		return access_token_getTime;
	}
	public void setAccess_token_getTime(long access_token_getTime) {
		this.access_token_getTime = access_token_getTime;
	}
	public int getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}
	
}