package steed.test;


import org.junit.Test;

import steed.ext.domain.terminal.TerminalUser;
import steed.ext.domain.user.User;
import steed.util.dao.DaoUtil;
import steed.util.digest.AESUtil;
import steed.util.digest.Md5Util;

public class DatabaseTest{
	@Test
	public void aesInit(){
		User user = DaoUtil.get(User.class, "admin");
		user.setPassword(AESUtil.aesEncode(Md5Util.Md5Digest("123456")));
		user.update();
		
		TerminalUser terminalUser = DaoUtil.get(TerminalUser.class, "admin");
		terminalUser.setPassword(AESUtil.aesEncode("123456"));
		terminalUser.update();
	}
}
