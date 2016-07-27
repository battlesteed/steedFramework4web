package steed.util.system;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import steed.util.base.PropertyUtil;
import steed.util.digest.AESUtil;

public class MyAuthenticator extends Authenticator {

	public MyAuthenticator() {
		super();
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		String password = AESUtil.aesDecode(PropertyUtil.getConfig("mail.password"));
		return new PasswordAuthentication(PropertyUtil.getConfig("mail.accout"), password);

	}
}
