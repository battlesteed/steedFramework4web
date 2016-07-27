package steed.util.base.test;

import steed.util.base.IOUtil;
import steed.util.base.PathUtil;
/**
 * 测试工具
 * @author 战马
 *
 */
public class TestUtil {
	/**
	 * 获取classpath下的test.txt中的文本，省去"要转义等麻烦
	 * @return
	 */
	public static String getTestText(){
		StringBuffer testSB = new StringBuffer();
		IOUtil io = new IOUtil();
		try {
			io.getBufferedReader(PathUtil.getClassesPath()+"test.txt");
			String temp;
			while ((temp = io.readLine()) != null) {
				testSB.append(temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			io.closeReader();
		}
		return testSB.toString();
	}
}
