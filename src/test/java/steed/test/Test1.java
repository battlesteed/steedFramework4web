package steed.test;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import steed.ext.domain.user.Role;
import steed.util.base.BaseUtil;
import steed.util.dao.DaoUtil;
import steed.util.digest.Base64Util;
import steed.util.document.QRCodeUtil;
import steed.util.other.Pinyin4J2;
import steed.util.wechat.SignUtil;

import com.google.zxing.WriterException;
public class Test1 {
	@Test
	public void testSteedDigest() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", "wxd225550ca5fc04db");
		map.put("timeStamp", 1395712654);
		map.put("nonceStr", "e61463f8efa94090b1f366cccfbbb444");
		map.put("package", "prepay_id=wx2015080820295309f0fff6080914366232");
		map.put("signType", "MD5");
		// map.put("paySign", );
		BaseUtil.out(SignUtil.sortAndAppendMap(map));
		BaseUtil.out(SignUtil.signMap(map, "MD5"));
		BaseUtil.out(SignUtil.signMap(map, "MD5").length());
		BaseUtil.out("70EA570631E4BB79628FBCA90534C63FF7FADD89".length());
	}

	@Test
	public void testSteedDigest2() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", "wx8888888888888888");
		map.put("timeStamp", "1414561699");
		map.put("nonceStr", "5K8264ILTKCH16CQ2502SI8ZNMTM67VS");
		map.put("package", "prepay_id=123456789");
		map.put("signType", "MD5");
		// map.put("paySign", );
		BaseUtil.out(SignUtil.sortAndAppendMap(map));
		BaseUtil.out(SignUtil.signMap(map, "MD5"));
		BaseUtil.out(SignUtil.signMap(map, "MD5").length());
		BaseUtil.out("C380BEC2BFD727A4B6845133519F3AD6".length());
	}


	@Test
	public void t() {
//		UtilsUtil.initUtils();
//		String temp = HttpUtil.getRequestString(HttpUtil.http_post,
//				"http://192.168.1.103:5568", null, null, "");
//		BaseUtil.out(temp + "yy");
		BaseUtil.out(new Pinyin4J2().getPinyin("屌"));
	}

	@Test
	public void testEncode() throws WriterException, IOException {
		String filePath = "D://";
		String fileName = "zxing.png";
		// JSONObject json = new JSONObject();
		// json.put(
		// "zxing","https://github.com/zxing/zxing/tree/zxing-3.0.0/javase/src/main/java/com/google/zxing");
		// json.put("author", "shihy");
		// String content = json.toJSONString();// 内容
		String content = "6923450657713";// 内容
		int width = 200; // 图像宽度
		int height = 200; // 图像高度
		String format = "png";// 图像类型
		new QRCodeUtil().getQrCode(filePath + fileName, content, width, height,
				format, false);
		System.out.println("输出成功.");
	}

	@Test
	public void test2() {
		BaseUtil.out(new SimpleDateFormat("yyyy/MM/dd/").format(new Date()));
		BaseUtil.out(Base64Util.base64Encode("测试"));
	}

	@Test
	public void test3() {
		String hql = "select new Map(sum(t.totalPrice) as totalPrice,count(t.orderNumber) as count,u.openid as openid) "
				+ "from Order1 t,cn.com.beyondstar.domain.wechat.WechatUser u where t.recommendCode in (select w.inviteCode from cn.com.beyondstar.domain.wechat.WechatUser w)";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("status_min_1", 1);
		param.put("status_max_1", 3);
		BaseUtil.out(DaoUtil.getCountHql(new StringBuffer(hql)));
	}

	@Test
	public void test4() throws IOException {
		File directory = new File(".");
		System.out.println("directory.getCanonicalPath();-->"
				+ directory.getCanonicalPath());
	}
	
	@Test
	public void test664(){
		BaseUtil.out(Set.class.isAssignableFrom(HashSet.class));
	}

	@Test
	public void test5() throws IOException {
		File directory = new File(".");
		System.out
				.println(new URLDecoder()
						.decode("{\"code\":0,\"data\":{\"country\":\"\u4e2d\u56fd\",\"country_id\":\"CN\",\"area\":\"\u534e\u5357\",\"area_id\":\"800000\",\"region\":\"\u5e7f\u4e1c\u7701\",\"region_id\":\"440000\",\"city\":\"\u6df1\u5733\u5e02\",\"city_id\":\"440300\",\"county\":\"\",\"county_id\":\"-1\",\"isp\":\"\u7535\u4fe1\",\"isp_id\":\"100017\",\"ip\":\"183.14.178.181\"}}"));
	}

	@Test
	public void test6() throws IOException {
		Role role = new Role("aa");
		Role role2 = new Role("aa");
		System.out.println(role.equals(role2));
		System.out.println(role.hashCode() == role2.hashCode());

		Map<Role, String> map = new HashMap<Role, String>();
		map.put(role, "");
		System.out.println(map.containsKey(role2));

	}
}
