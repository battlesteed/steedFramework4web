package steed.util.wechat.domain.resp;

/** 
 * 语音消息 
 *  
 */  
public class VoiceMessage extends BaseMessage {  
	 // 回复的消息内容  
//	@XStreamImplicit(itemFieldName="MediaId",keyFieldName="MediaId")
//    private List Image = new ArrayList();
	private Vice Voice = new Vice();
	public VoiceMessage() {
		super();
		MsgType = "voice";
	}

	public String getMediaId() {
		return Voice.MediaId;
	}

	public void setMediaId(String mediaId) {
		Voice.MediaId = mediaId;
	}  
	class Vice{
		private String MediaId;
	}

	
}  