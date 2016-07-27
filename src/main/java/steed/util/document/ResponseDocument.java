package steed.util.document;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 
 * @author 战马
 *
 */
public class ResponseDocument {

	public static void setHeader(String fileName,HttpServletResponse response,HttpServletRequest request) {
    	try {
    		String upperCase = request.getHeader("User-Agent").toUpperCase();
			if (upperCase.indexOf("MSIE") > 0 || upperCase.indexOf("LIKE GECKO") > 0 ) {  
    			response.addHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "UTF-8"));
    		} else {  
    			response.addHeader("Content-Disposition", "attachment;filename="+new String(fileName.getBytes("UTF-8"), "ISO8859-1"));
    		}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}  
	}
}
