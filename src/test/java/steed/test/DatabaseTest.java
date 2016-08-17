package steed.test;


import org.junit.Test;

import steed.util.base.BaseUtil;
import steed.util.digest.AESUtil;
import steed.util.digest.Md5Util;

public class DatabaseTest{
	@Test
	public void aesInit(){
		BaseUtil.out("admin密码", AESUtil.aesEncode(Md5Util.Md5Digest("123456")));
		BaseUtil.out("终端用户密码", AESUtil.aesEncode("123456"));
	}
}
