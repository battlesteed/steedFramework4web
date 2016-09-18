package steed.util.wechat.domain.resp;

import steed.util.wechat.MessageUtil;

/** 
 * 文本消息 
 */  
public class TextMessage extends BaseMessage {  
    // 回复的消息内容  
    private String Content;  
  
    public TextMessage() {
		super();
		setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
	}

	public String getContent() {  
        return Content;  
    }  
  
    public void setContent(String content) {  
        Content = content;  
    }  
}  