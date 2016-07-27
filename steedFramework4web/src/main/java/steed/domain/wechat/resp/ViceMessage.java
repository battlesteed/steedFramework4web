package steed.domain.wechat.resp;



/** 
 * 文本消息 
 *  
 */  
public class ViceMessage extends BaseMessage {  
    // 回复的消息内容  
//	@XStreamImplicit(itemFieldName="MediaId",keyFieldName="MediaId")
//    private List Image = new ArrayList();
	private Vice Vice = new Vice();
	public ViceMessage() {
		super();
		MsgType = "vice";
	}

	public String getMediaId() {
		return Vice.MediaId;
	}

	public void setMediaId(String mediaId) {
		Vice.MediaId = mediaId;
	}  
	class Vice{
		private String MediaId;
	}
     
}  