package steed.util.wechat.domain.sys;

public class MessageReceive {
	private String ToUserName;
	private String FromUserName;
	private Long CreateTime;
	private String MsgType;
	private String Event;
	private String EventKey;
	private String Content;
	private String MediaId;
	private String Format;
	private String MsgSignature;
	private String Recognition;
	private Long TimeStamp;
	private String Encrypt;
	private String Nonce;
	private String MsgId;
	/** 微信小店订单字段*/
	private String OrderId;
	private String OrderStatus;
	private String ProductId;
	private String SkuInfo;
	/**
	 * 二维码的ticket，可用来换取二维码图片 
	 */
	private String Ticket;
	/** 微信小店订单字段*/
	private ScanCodeInfo ScanCodeInfo;
	
	/*******#地理位置上报*******/
	/**
	 * 地理位置纬度
	 */
	private Double Latitude; 
	/**
	 * 地理位置经度
	 */
	private Double Longitude;
	/**
	 * 地理位置精度 
	 */
	private Double Precision;
	
	/*******\地理位置上报*******/
	
	private String MenuId;
	
	public String getMenuId() {
		return MenuId;
	}
	public void setMenuId(String menuId) {
		MenuId = menuId;
	}
	public String getToUserName() {
		return ToUserName;
	}
	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}
	public String getContent() {
		return Content;
	}
	
	public Double getLatitude() {
		return Latitude;
	}
	public void setLatitude(Double latitude) {
		Latitude = latitude;
	}
	public String getTicket() {
		return Ticket;
	}
	public void setTicket(String ticket) {
		Ticket = ticket;
	}
	public Double getLongitude() {
		return Longitude;
	}
	public void setLongitude(Double longitude) {
		Longitude = longitude;
	}
	public Double getPrecision() {
		return Precision;
	}
	public void setPrecision(Double precision) {
		Precision = precision;
	}
	public String getMsgSignature() {
		return MsgSignature;
	}
	public String getOrderId() {
		return OrderId;
	}
	public String getOrderStatus() {
		return OrderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		OrderStatus = orderStatus;
	}
	public String getProductId() {
		return ProductId;
	}
	public void setProductId(String productId) {
		ProductId = productId;
	}
	public String getSkuInfo() {
		return SkuInfo;
	}
	public void setSkuInfo(String skuInfo) {
		SkuInfo = skuInfo;
	}
	public void setOrderId(String orderId) {
		OrderId = orderId;
	}
	public void setMsgSignature(String msgSignature) {
		MsgSignature = msgSignature;
	}
	public void setContent(String content) {
		Content = content;
	}
	public String getMsgId() {
		return MsgId;
	}
	public void setMsgId(String msgId) {
		MsgId = msgId;
	}
	public String getMediaId() {
		return MediaId;
	}
	public String getFormat() {
		return Format;
	}
	public String getRecognition() {
		return Recognition;
	}
	public void setRecognition(String recognition) {
		Recognition = recognition;
	}
	public void setFormat(String format) {
		Format = format;
	}
	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}
	public Long getTimeStamp() {
		return TimeStamp;
	}
	public void setTimeStamp(Long timeStamp) {
		TimeStamp = timeStamp;
	}
	public String getEncrypt() {
		return Encrypt;
	}
	public void setEncrypt(String encrypt) {
		Encrypt = encrypt;
	}
	public String getNonce() {
		return Nonce;
	}
	public void setNonce(String nonce) {
		Nonce = nonce;
	}
	public String getFromUserName() {
		return FromUserName;
	}
	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}
	public Long getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(Long createTime) {
		CreateTime = createTime;
	}
	public String getMsgType() {
		return MsgType;
	}
	public void setMsgType(String msgType) {
		MsgType = msgType;
	}
	public String getEvent() {
		return Event;
	}
	public void setEvent(String event) {
		Event = event;
	}
	public String getEventKey() {
		return EventKey;
	}
	public void setEventKey(String eventKey) {
		EventKey = eventKey;
	}
	public ScanCodeInfo getScanCodeInfo() {
		return ScanCodeInfo;
	}
	public void setScanCodeInfo(ScanCodeInfo scanCodeInfo) {
		ScanCodeInfo = scanCodeInfo;
	}
}
