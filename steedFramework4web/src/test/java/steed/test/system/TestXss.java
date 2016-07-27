package steed.test.system;

import org.junit.Test;

import steed.util.base.BaseUtil;
import steed.util.base.test.TestEfficiency;
import steed.util.base.test.TestUtil;
import steed.util.system.ValidateUtil;

public class TestXss {
	@Test
	public void test1(){
		TestEfficiency test = new TestEfficiency();
		test.begin();
		String cleanXss = ValidateUtil.cleanXss(TestUtil.getTestText());
		test.endAndOutUsedTime(null);
		BaseUtil.out(cleanXss);
	}
}
