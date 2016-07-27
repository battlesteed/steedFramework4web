package steed.util.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 * 
 * @author 战马
 *
 */
public class ExcelUtil {
	public static void createExcel(List<List<Object>> excels,String title,OutputStream out){
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(title);
        sheet.setDefaultColumnWidth((short) 15);
        for (int i = 0; i < excels.size(); i++) {
        	XSSFRow row = sheet.createRow(i);
        	List<Object> beanRow = excels.get(i);
        	for (int j = 0; j < beanRow.size(); j++) {
        		XSSFCell cell = row.createCell(j);
				Object str = beanRow.get(j);
				/*if (str != null) {
					if (str instanceof Date) {
						cell.setCellValue(str);
					}
				}*/
//				cell.setCellValue(str == null ? "":str);
				setCellValue(str, cell,workbook);
			}
		}
        
        try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        
	}
	
	
	public static void setCellValue(Object obj,XSSFCell cell,XSSFWorkbook book){
		if (obj != null) {
			if (obj instanceof Date) {
				cell.setCellValue((Date)obj);
				XSSFCellStyle dateCellStyle=book.createCellStyle();
				short df= book.createDataFormat().getFormat("yyyy-MM-dd HH:mm"); 
				dateCellStyle.setDataFormat(df);
				cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
				cell.setCellStyle(dateCellStyle);
//				cell.
				//HSSFDateUtil.s
			}else if(obj instanceof Boolean){
				cell.setCellValue((Boolean) obj);
				cell.setCellType(XSSFCell.CELL_TYPE_BOOLEAN);
			}else if (obj instanceof String) {
				cell.setCellValue((String)obj);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			}else if (obj instanceof Calendar) {
				cell.setCellValue((Calendar)obj);
			}else if (obj instanceof Double || obj instanceof Integer 
					|| obj instanceof Float || obj instanceof Long
					|| obj instanceof Short) {
				cell.setCellValue(Double.parseDouble(obj + ""));
				cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
			}else if (obj instanceof RichTextString) {
				cell.setCellValue((RichTextString)obj);
			}else {
				cell.setCellValue(obj.toString());
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			}
		}else {
			cell.setCellType(XSSFCell.CELL_TYPE_BLANK);
		}
	}
	
	public static Workbook create(InputStream in) throws IOException,InvalidFormatException {
        if (!in.markSupported()) {
            in = new PushbackInputStream(in, 8);
        }
        if (POIFSFileSystem.hasPOIFSHeader(in)) {
            return new HSSFWorkbook(in);
        }
        if (POIXMLDocument.hasOOXMLHeader(in)) {
            return new XSSFWorkbook(OPCPackage.open(in));
        }
        throw new IllegalArgumentException("你的excel版本目前poi解析不了");
    }
	
	private static String convertCell(Cell cell){
		String cellValue = "";
		if (cell==null) {
			return cellValue;
		}
		
		switch (cell.getCellType()) {
		case XSSFCell.CELL_TYPE_NUMERIC:
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				Date d = cell.getDateCellValue();
				DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
				cellValue = formater.format(d);
			}else {
				Double numericCellValue = cell.getNumericCellValue();
				if (numericCellValue.longValue() != numericCellValue.doubleValue()) {
					DecimalFormat df = new DecimalFormat("0.00000");
					cellValue = df.format(numericCellValue);
				}else {
					cellValue = numericCellValue.longValue() + "";
				}
			}
			//cellValue=cellValue.substring(0, cellValue.lastIndexOf("."));
			break;
		case XSSFCell.CELL_TYPE_BOOLEAN:
			cellValue = Boolean.valueOf(cell.getBooleanCellValue()).toString();
			break;
		case XSSFCell.CELL_TYPE_ERROR:
			cellValue = String.valueOf(cell.getErrorCellValue());
			break;
		case XSSFCell.CELL_TYPE_STRING:
//			cellValue = cell.getStringCellValue();
//			break;
		case XSSFCell.CELL_TYPE_BLANK:
//			cellValue = cell.getStringCellValue();
//			break;
		default:
			cellValue = cell.getStringCellValue();
			break;
		}
		return cellValue;
	}
	public static List<List<String>> parseExcel(File file){
		try {
			return parseExcel(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public static List<List<String>> parseExcel(InputStream in){
		List<List<String>> excels = new ArrayList<List<String>>();
		try {
			Workbook workbook = (Workbook) create(in);
			Sheet sheet = null;
			Row row = null;
			Cell cell = null;
			
			String tempString = null;
			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
					sheet=workbook.getSheetAt(i);
					if (sheet!=null) {
						for (int j = 0; j <= sheet.getLastRowNum(); j++) {
							if (i!=0&&j==0) {
								continue;
							}
							row= sheet.getRow(j);
							if (row!=null) {
								List<String> rowlList = new ArrayList<String>();
								for (int j2 = 0; j2 <= row.getLastCellNum(); j2++) {
									cell=row.getCell(j2);
	//								if (cell!=null) {
										tempString = convertCell(cell);
										//System.out.println(tempString);
										rowlList.add(tempString);
	//								}
										//Excel temp = new Excel();
								}
								excels.add(rowlList);
							}
						}
					}
			}
			return excels;
		} catch (InvalidFormatException e) {
			e.printStackTrace();
			return null;
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}
		
	}
	public static List<Map<String, String>> parseExcelIntoMap(InputStream in){
		return getMapParseResutl(parseExcel(in));
	}
	public static List<Map<String, String>> parseExcelIntoMap(File file){
		return getMapParseResutl(parseExcel(file));
	}


	private static List<Map<String, String>> getMapParseResutl(
			List<List<String>> parseExcel) {
		List<Map<String, String>> excels = new ArrayList<Map<String, String>>();
		List<String> list = parseExcel.get(0);
		for (int i = 1; i < parseExcel.size(); i++) {
			List<String> temp = parseExcel.get(i);
			Map<String, String> map = new HashMap<String, String>();
			for (int j = 0; j < list.size(); j++) {
				if (temp.size() > j) {
					map.put(list.get(j), temp.get(j));
				}else {
					break;
				}
			}
			excels.add(map);
		}
		return excels;
	}
}
