package steed.ext.action.client;

import steed.action.BaseAction;
import steed.ext.domain.terminal.ClientMessage;
import steed.ext.domain.terminal.TerminalUser;
import steed.util.system.DataCacheUtil;

public class BaseClientAction extends BaseAction{
	private static final long serialVersionUID = -2193806489289321711L;
	/**
	 * 获取当前登录的终端用户
	 * @return
	 */
	protected TerminalUser getLoginTerminalUser(){
		return (TerminalUser) DataCacheUtil.getData(getRequestParameter("token"), "TerminalToken");
	}
	/**
	 * 给客户端返回接口调用消息
	 * @param errCode 错误码,0为成功,
	 * @param message 提示消息,可以为null,为null时成功则提示成功,失败则提示出错了,
	 * @param content 返回客户端的实体类,
	 */
	protected void writeClientMessage(int errCode,String message,Object content) {
		ClientMessage clientMessage = new ClientMessage();
		clientMessage.setStatusCode(errCode);
		clientMessage.setMessage(message);
		if (message == null) {
			if (errCode == 0) {
				clientMessage.setMessage("成功");
			}else {
				clientMessage.setMessage("出错了");
			}
		}
		clientMessage.setContent(content);
		writeObjectMessage(clientMessage);
	}
}
