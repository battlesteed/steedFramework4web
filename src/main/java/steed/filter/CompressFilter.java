package steed.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 把css,js,html,jsp等压缩成gzip格式
 * @author 战马
 *
 */
public class CompressFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse) resp;
		
		if(!isGziSupported(request)){
			chain.doFilter(request, response);
		}else{
			CompressWrapper wrapper = new CompressWrapper(response);
			chain.doFilter(request, wrapper);
			byte[] buffer = wrapper.getBuffer();
//			int len = buffer.length;
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			GZIPOutputStream gZip = new GZIPOutputStream(out);
			gZip.write(buffer);
			gZip.flush();
 			gZip.close();
			
			byte[] gZipBuffer = out.toByteArray();
//			len = gZipBuffer.length;
			response.setHeader("content-encoding", "gzip");
			response.setContentLength(gZipBuffer.length);
			
//			response.getOutputStream().write(buffer);
			response.getOutputStream().write(gZipBuffer);
		}
	}
	
	/**
	 * 是否支持gzip
	 * @param req
	 * @return
	 */
	private boolean isGziSupported(HttpServletRequest req){
		String browserEncodings = req.getHeader("Accept-Encoding");
        return ((browserEncodings != null) && (browserEncodings.indexOf("gzip") != -1));
	}
	
	
	

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}
	
	
	
	
	

}
