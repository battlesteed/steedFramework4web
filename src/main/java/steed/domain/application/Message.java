package steed.domain.application;

import steed.hibernatemaster.domain.BaseDomain;
/**
 * 前端提示消息实体类
 * @author 战马
 *
 */
public class Message extends BaseDomain{
	private static final long serialVersionUID = 7956968695852861290L;
	private Integer errCode;
	private String message;
	private String url;
	private String title;
	public final static int type_save = 3;
	public final static int type_update = 1;
	public final static int type_delete = 2;
	public final static int type_delete_fail = -2;
	public final static int type_update_fail = -1;
	public final static int type_save_fail = -3;
	
	/*public Message(Integer errCode, String message, String url) {
		this.errCode = errCode;
		this.message = message;
		this.url = url;
	}*/
	
	
	public Message(Integer errCode,String message, String title) {
		this.errCode = errCode;
		this.message = message;
		this.title = title;
	}


	public Message(Integer errCode, String message, String url, String title) {
		this.errCode = errCode;
		this.message = message;
		this.url = url;
		this.title = title;
	}
	public Message(int type) {
		dealType(type);
	}


	private void dealType(int type) {
		switch (type) {
		case type_save:
			message = "添加成功！";
			title = "添加成功";
			errCode = 0;
			break;
		case type_update:
			message = "修改成功！";
			title = "修改成功";
			errCode = 0;
			break;
		case type_delete:
			message = "删除成功！";
			title = "删除成功";
			errCode = 0;
			break;
		case type_save_fail:
			message = "添加失败！";
			title = "添加失败";
			errCode = -1;
			break;
		case type_update_fail:
			message = "修改失败！";
			title = "修改失败";
			errCode = -1;
			break;
		case type_delete_fail:
			message = "删除失败！";
			title = "删除失败";
			errCode = -1;
			break;
		default:
			break;
		}
	}
	public Message(String url, int type) {
		this.url = url;
		dealType(type);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Message(Integer errCode, String message) {
		this.errCode = errCode;
		this.message = message;
	}
	public Message() {
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getErrCode() {
		return errCode;
	}
	public void setErrCode(Integer errCode) {
		this.errCode = errCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public boolean isSuccess() {
		return errCode == 0;
	}
	
}
