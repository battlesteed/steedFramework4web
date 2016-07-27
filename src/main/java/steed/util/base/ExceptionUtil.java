package steed.util.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import steed.exception.runtime.system.FrameworkException;


public class ExceptionUtil {
	private static final Logger log = LoggerFactory.getLogger(ExceptionUtil.class);
	public static void throwRuntimeException(Object message,Logger logger) {
		RuntimeException runtimeException = new RuntimeException(message.toString());
		logger.error(runtimeException.toString(),runtimeException);
		throw runtimeException;
	}
	public static void throwFrameworkException(String message) {
		FrameworkException frameworkException = new FrameworkException(message);
		log.error(message,frameworkException);
		throw frameworkException;
	}
	public static void throwFrameworkException(Exception e) {
		FrameworkException frameworkException = new FrameworkException(e);
		log.error(e.getMessage(),frameworkException);
		throw frameworkException;
	}
	public static void compileException2RuntimeException(Exception e) {
		RuntimeException runtimeException = new RuntimeException(e);
		throw runtimeException;
	}
}
