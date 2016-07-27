package steed.test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.junit.Test;

import steed.ext.domain.user.User;
import steed.util.base.BaseUtil;
import steed.util.base.test.TestUtil;
import steed.util.dao.DaoUtil;
import steed.util.digest.AESUtil;
import steed.util.http.HttpUtil;
import steed.util.system.EMailUtil;
import steed.util.wechat.WechatInterfaceInvokeUtil;

public class BaseTest {
	@Test
	public void test1(){
		/*BaseUtil.out(AESUtil.aesEncode("battle_steed@123"));
		try {
			EMailUtil.sendEmail("测试", "测试", "1255372919@qq.com");
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}*/
		String requestString = HttpUtil.getRequestString(HttpUtil.http_get, "https://sp0.baidu.com/9_Q4sjW91Qh3otqbppnN2DJv/pae/channel/data/asyncqury?appid=4001&com=yuantong&nu=881443775034378914", null, null);
		BaseUtil.out(requestString);
	}
	
	@Test
	public void test2(){
//		BaseUtil.outJson(WechatInterfaceInvokeUtil.getTempQrcode(60*10, 1));
//		BaseUtil.outJson(WechatInterfaceInvokeUtil.getPermanentQrcode("dddd", null));
//		DaoUtil.get(People.class, key);
		DaoUtil.listAllObj(User.class, Arrays.asList("nickName","name"), Arrays.asList("sex","phoneNumber"));
	}
	
	@Test
	public void test3(){
		BaseUtil.out(TestUtil.getTestText().replaceAll("\n", ""));
		BaseUtil.out(AESUtil.aesEncode("hzzz@huangzuhome.com"));
		BaseUtil.out(AESUtil.aesEncode(TestUtil.getTestText().replaceAll("\n", "")));
	}
}
