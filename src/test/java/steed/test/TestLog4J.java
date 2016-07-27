package steed.test;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import steed.util.UtilsUtil;
import steed.util.base.BaseUtil;

public class TestLog4J {
	private static Logger logger;
	@Test
	public void test1(){
		UtilsUtil.initUtils();
		BaseUtil.out(System.getProperty("log_dir"));
		logger = Logger.getLogger(TestLog4J.class);
		logger.debug("中午",new RuntimeException("中文"));
	}
	@Test
	public void test2(){
		UtilsUtil.initUtils();
		BaseUtil.out(System.getProperty("log_dir"));
//		logger = Logger.getLogger(TestLog4J.class);
		LoggerFactory.getLogger(TestLog4J.class).debug("中午",new RuntimeException("中文"));
	}
}
