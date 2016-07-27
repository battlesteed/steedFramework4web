package steed.util.wechat.domain.send;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 消息统计
 * 
 * @author 战马
 * 
 */
public class ArticleSummarySend extends BaseWechatSend{
	private String begin_date;

	private String end_date;

	public void setDate(String date) {
		this.begin_date = date;
		this.end_date = date;
	}
	public void setDate(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String format = dateFormat.format(date);
		this.begin_date = format;
		this.end_date = format;
	}

	public String getBegin_date() {
		return this.begin_date;
	}

	public String getEnd_date() {
		return this.end_date;
	}
}
