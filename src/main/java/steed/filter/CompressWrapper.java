package steed.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
//import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import steed.exception.runtime.CompressException;
import steed.util.base.PropertyUtil;

public class CompressWrapper extends HttpServletResponseWrapper {

	private ByteArrayOutputStream stream = new ByteArrayOutputStream();
	private PrintWriter writer;
	private ServletOutputStream out;

	public CompressWrapper(HttpServletResponse response) {
		super(response);
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if (out == null) {
			out = new ServletOutputStream() {

				@Override
				public void write(int b) throws IOException {
					stream.write(b);
				}
				 /**
				  * j2ee1.7及以上才能用
				  * @param arg0
				  */
				@Override
				public void setWriteListener(WriteListener arg0) {

				}

				public boolean isReady() {
					return false;
				}
			};
		}

		return out;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		if (writer == null) {
			writer = new PrintWriter(new OutputStreamWriter(stream, PropertyUtil.getConfig("characterSet")));
		}
		return this.writer;
	}

	public byte[] getBuffer() {

		try {
			if(writer != null){
				writer.flush();
				writer.close();
			}
			if (stream != null) {
				stream.flush();
				return stream.toByteArray();
			}
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			throw new CompressException();
		}
	}

}
