package steed.util.base.test;

import java.io.File;

import steed.util.base.PathUtil;
import steed.util.file.FileUtil;
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
		return FileUtil.readFile(new File(PathUtil.getClassesPath()+"test.txt"));
	}
}
