package steed.domain.application;

import steed.domain.BaseDomain;
/**
 * 前端提示消息实体类
 * @author 战马
 *
 */
public class DWZMessage extends BaseDomain{
	private static final long serialVersionUID = 7956968695852861290L;
	private Integer statusCode;
	private String message;
	private String forwardUrl;
	private String navTabId;
	private String callbackType;
	private Object data;
	private String title;
	
	public static final String callbackType_forward = "forward";
	public static final String callbackType_closeCurrent = "closeCurrent";
	
	public static final int statusCode_fail = 300;
	public static final int statusCode_success = 200;
	
	public final static int type_save = 3;
	public final static int type_update = 1;
	public final static int type_delete = 2;
	public final static int type_delete_fail = -2;
	public final static int type_update_fail = -1;
	public final static int type_save_fail = -3;
	
	public Boolean getIsSuccess() {
		return statusCode != null && statusCode == statusCode_success;
	}
	public void setIsSuccess(Boolean isSuccess) {
		this.statusCode = isSuccess?200:300;
	}
	private void dealType(int type) {
		switch (type) {
		case type_save:
			setMessageAndTitle("添加成功！");
			statusCode = 200;
			break;
		case type_update:
			setMessageAndTitle("修改成功！");
			statusCode = 200;
			break;
		case type_delete:
			setMessageAndTitle("删除成功！");
			statusCode = 200;
			break;
		case type_save_fail:
			setMessageAndTitle("添加失败！");
			statusCode = 300;
			break;
		case type_update_fail:
			setMessageAndTitle("修改失败！");
			statusCode = 300;
			break;
		case type_delete_fail:
			setMessageAndTitle("删除失败！");
			statusCode = 300;
			break;
		default:
			break;
		}
	}
	public DWZMessage(int type) {
		dealType(type);
	}
	
	public DWZMessage(boolean isSuccess) {
		statusCode = isSuccess?200:300;
	}
	
	public DWZMessage(Integer statusCode, String message, String title) {
		super();
		this.statusCode = statusCode;
		this.message = message;
		this.title = title;
	}
	public DWZMessage(Integer statusCode, String message, String forwardUrl,
			String navTabId, String callbackType, Object data, String title) {
		super();
		this.statusCode = statusCode;
		this.message = message;
		this.forwardUrl = forwardUrl;
		this.navTabId = navTabId;
		this.callbackType = callbackType;
		this.data = data;
		this.title = title;
	}
	public String getCallbackType() {
		return callbackType;
	}
	public void setCallbackType(String callbackType) {
		this.callbackType = callbackType;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	/**
	 * 
	 * @param typeFailOrSucceed
	 * @param isSucceed
	 */
	public DWZMessage(int typeFailOrSucceed,boolean isSucceed) {
		if (isSucceed) {
			dealType(Math.abs(typeFailOrSucceed));
		}else {
			dealType(-Math.abs(typeFailOrSucceed));
		}
	}
	public DWZMessage(String messagePrefix,boolean isSucceed) {
		if (isSucceed) {
			setMessageAndTitle(messagePrefix+"成功！！");
			statusCode = statusCode_success;
		}else {
			setMessageAndTitle(messagePrefix+"失败！！");
			statusCode = statusCode_fail;
		}
	}
	public DWZMessage() {
	}
	public Integer getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}
	public void setStatusCode(boolean isSuccess) {
		this.statusCode = isSuccess?200:300;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public void setMessageAndTitle(String message) {
		this.message = message;
		this.title = message;
	}
	public String getForwardUrl() {
		return forwardUrl;
	}
	public void setForwardUrl(String forwardUrl) {
		this.forwardUrl = forwardUrl;
	}
	public String getNavTabId() {
		return navTabId;
	}
	public void setNavTabId(String navTabId) {
		this.navTabId = navTabId;
	}
}
