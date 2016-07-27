package steed.util.digest;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import steed.exception.runtime.system.FrameworkException;
import steed.util.base.PropertyUtil;
import steed.util.base.StringUtil;

public class AESUtil {
	/**
	 * 加密
	 * 
	 * @param content
	 *            需要加密的内容
	 * @param password
	 *            加密密码
	 * @return
	 */
	public static byte[] encrypt(byte[] byteContent) {
		return encrypt(byteContent, getSystemKey());
	}

	/**
	 * 加密
	 * 
	 * @param content
	 *            需要加密的内容
	 * @param password
	 *            加密密码
	 * @return
	 */
	public static byte[] encrypt(byte[] byteContent, String key) {
		try {
			byte[] enCodeFormat = getSecretKey(key).getEncoded();
			SecretKeySpec kee = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			// byte[] byteContent =
			// content.getBytes(ApplicationStaticParam.SYSTEM.CHARACTER_SET);
			cipher.init(Cipher.ENCRYPT_MODE, kee);// 初始化
			byte[] result = cipher.doFinal(byteContent);
			return result; // 加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 * 
	 * @param content
	 *            待解密内容
	 * @param password
	 *            解密密钥
	 * @return
	 */
	public static byte[] decrypt(byte[] content) {
		return decrypt(content, getSystemKey());
	}

	/**
	 * 解密
	 * 
	 * @param content
	 *            待解密内容
	 * @param password
	 *            解密密钥
	 * @return
	 */
	public static byte[] decrypt(byte[] content, String key) {
		try {
			byte[] enCodeFormat = getSecretKey(key).getEncoded();
			SecretKeySpec kee = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, kee);// 初始化
			byte[] result = cipher.doFinal(content);
			return result; // 加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*private static SecretKey getSecretKey() {
		return getSecretKey(getSystemKey());
	}*/

	private static String getSystemKey() {
		return PropertyUtil.getConfig("aesKey");
	}

	private static SecretKey getSecretKey(String key) {
		KeyGenerator kgen;
		try {
			kgen = KeyGenerator.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new FrameworkException(e);
		}
		kgen.init(128,
				new SecureRandom(StringUtil.getSystemCharacterSetBytes(key)));
		SecretKey secretKey = kgen.generateKey();
		return secretKey;
	}

	/**
	 * aes解密
	 * 
	 * @param message
	 * @return
	 */
	public static byte[] aesDecodeByte(String message) {
		return decrypt(Base64Util.base64DecodeByte(message));
	}

	/**
	 * aes加密
	 * 
	 * @param message
	 * @return
	 */
	public static byte[] aesEncode1(String message,String key) {
		try {
			return encrypt(message.getBytes(PropertyUtil
					.getConfig("characterSet")),key);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * aes加密
	 * 
	 * @param message
	 * @return
	 */
	public static byte[] aesEncode1(String message) {
		try {
			return encrypt(message.getBytes(PropertyUtil
					.getConfig("characterSet")),getSystemKey());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * aes加密
	 * 
	 * @param message
	 * @return
	 */
	public static byte[] aesEncodeByte1(byte[] message) {
		return encrypt(message);
	}

	/**
	 * aes加密
	 * 
	 * @param message
	 * @return
	 */
	public static String aesEncodeByte(byte[] message) {
		return Base64Util.base64EncodeByte(aesEncodeByte1(message));
	}

	/**
	 * aes解密
	 * 
	 * @param message
	 * @return
	 */
	public static String aesDecode(String message) {
		return StringUtil.getSystemCharacterSetString(decrypt(Base64Util
				.base64DecodeByte(message)));
	}

	/**
	 * aes加密
	 * 
	 * @param message
	 * @return
	 */
	public static String aesEncode(String message) {
		return Base64Util.base64EncodeByte(aesEncode1(message));
	}

	/**
	 * aes加密
	 * 
	 * @param message
	 * @return
	 */
	public static String aesEncode(String message, String key) {
		return Base64Util.base64EncodeByte(aesEncode1(message,key));
	}

	//
	// /**将二进制转换成16进制
	// * @param buf
	// * @return
	// */
	// private static String parseByte2HexStr(byte buf[]) {
	// StringBuffer sb = new StringBuffer();
	// for (int i = 0; i < buf.length; i++) {
	// String hex = Integer.toHexString(buf[i] & 0xFF);
	// if (hex.length() == 1) {
	// hex = '0' + hex;
	// }
	// sb.append(hex.toUpperCase());
	// }
	// return sb.toString();
	// }
	//
	// /**将16进制转换为二进制
	// * @param hexStr
	// * @return
	// */
	// private static byte[] parseHexStr2Byte(String hexStr) {
	// if (hexStr.length() < 1)
	// return null;
	// byte[] result = new byte[hexStr.length()/2];
	// for (int i = 0;i< hexStr.length()/2; i++) {
	// int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
	// int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
	// result[i] = (byte) (high * 16 + low);
	// }
	// return result;
	// }

}