package steed.test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import steed.util.base.PathUtil;
import steed.util.document.ExcelUtil;
import steed.util.document.MergedRegion;

public class ExcelTest {
	/**
	 * 用合并表格命令实体类生成复杂Excel
	 */
	@Test
	public void testMergedRegion(){
		List<List<Object>> excels = new ArrayList<>();
		List<Object> header = new ArrayList<>();
		header.add("表头1");
		header.add("表头2");
		header.add("表头3");
		header.add("表头4");
		header.add("表头5");
		excels.add(header);
		
		List<Object> row = new ArrayList<>();
		row.add("内容1");
		row.add("内容1");
		row.add("占两行");
		row.add("内容1");
		row.add("内容1");
		excels.add(row);
		List<Object> row2 = new ArrayList<>();
		row2.add("占两格");
		//因为上面row2第一个格子占两个格,所以这里被占用了,要add一个MergedRegion合并表格实体类
		row2.add(new MergedRegion(2, 2, 0, 1));
		//因为上面row的格子占两行,所以这里被占用了,要add一个MergedRegion合并表格实体类
		row2.add(new MergedRegion(1, 2, 2, 2));
		
		excels.add(row2);
		
		try {
			ExcelUtil.createExcel(excels, "testMergedRegion", new FileOutputStream("D:\\test.xlsx"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
