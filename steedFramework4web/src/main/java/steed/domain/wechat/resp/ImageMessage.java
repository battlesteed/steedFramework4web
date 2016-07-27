package steed.domain.wechat.resp;



/** 
 * 文本消息 
 *  
 */  
public class ImageMessage extends BaseMessage {  
    // 回复的消息内容  
//	@XStreamImplicit(itemFieldName="MediaId",keyFieldName="MediaId")
//    private List Image = new ArrayList();
	private Image Image = new Image();
	public ImageMessage() {
		super();
		MsgType = "image";
	}

	public String getMediaId() {
		return Image.MediaId;
	}

	public void setMediaId(String mediaId) {
		Image.MediaId = mediaId;
	}  
	class Image{
		private String MediaId;
	}
     
}  