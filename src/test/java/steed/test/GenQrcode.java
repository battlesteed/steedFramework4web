package steed.test;

import java.io.BufferedWriter;
import java.io.IOException;

import org.junit.Test;

import steed.util.base.IOUtil;
import steed.util.base.StringUtil;

public class GenQrcode {
	
	@Test
	public void gen(){
		IOUtil ioUtil = new IOUtil();
		try {
			BufferedWriter bufferedWriter = ioUtil.getBufferedWriter("D:\\qrcode.txt");
			for(int i = 0;i < 10000;i++){
				bufferedWriter.write(StringUtil.getSecureRandomString());
				bufferedWriter.write("\r\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			ioUtil.closeIO();
		}
		
	}
}
