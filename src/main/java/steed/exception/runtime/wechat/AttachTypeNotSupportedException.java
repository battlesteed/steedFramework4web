package steed.exception.runtime.wechat;

import steed.exception.runtime.BaseRuntimeException;

public class AttachTypeNotSupportedException extends BaseRuntimeException{
	private static final long serialVersionUID = 8770874163744504532L;

	public AttachTypeNotSupportedException() {
		super();
	}

	public AttachTypeNotSupportedException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AttachTypeNotSupportedException(String message, Throwable cause) {
		super(message, cause);
	}

	public AttachTypeNotSupportedException(String message) {
		super(message);
	}

	public AttachTypeNotSupportedException(Throwable cause) {
		super(cause);
	}

}
