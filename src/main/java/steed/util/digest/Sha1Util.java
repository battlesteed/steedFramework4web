package steed.util.digest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import steed.util.base.StringUtil;

public class Sha1Util {
	public static String sha1Digest(String data){
		return DigestUtil.byte2hex(sha1Digest(StringUtil.getSystemCharacterSetBytes(data)));
	}
	
	public static byte[] sha1Digest(byte[] data){
		   MessageDigest sha1;
		try {
			sha1 = MessageDigest.getInstance("SHA-1");
			return sha1.digest(data);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new RuntimeException("获取SHA-1加密实例失败!!!");
		}        
	}
}
