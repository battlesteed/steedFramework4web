package steed.exception.runtime.wechat;

import steed.exception.runtime.BaseRuntimeException;

public class WechatIoException extends BaseRuntimeException{

	private static final long serialVersionUID = -745775895984684305L;

	public WechatIoException() {
		super();
	}

	public WechatIoException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public WechatIoException(String message, Throwable cause) {
		super(message, cause);
	}

	public WechatIoException(String message) {
		super(message);
	}

	public WechatIoException(Throwable cause) {
		super(cause);
	}

}
