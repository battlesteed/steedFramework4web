package steed.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.hibernate.Session;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import steed.domain.wechat.WechatUser;
import steed.ext.domain.information.Information;
import steed.ext.domain.information.Programa;
import steed.ext.domain.system.Menu;
import steed.ext.domain.user.User;
import steed.ext.domain.user.Power;
import steed.util.UtilsUtil;
import steed.util.base.BaseUtil;
import steed.util.base.DomainUtil;
import steed.util.base.PathUtil;
import steed.util.base.StringUtil;
import steed.util.base.test.TestEfficiency;
import steed.util.base.test.TestUtil;
import steed.util.dao.DaoUtil;
import steed.util.dao.HibernateUtil;
import steed.util.digest.AESUtil;
import steed.util.digest.Md5Util;
import steed.util.http.HttpUtil;
import steed.util.system.LogisticsUtil;
import steed.util.system.LogisticsUtil.QueryResult;
import steed.util.system.TaskUtil;
import steed.util.system.ValidateUtil;
import steed.util.wechat.WechatConstantParamter;


public class Test {
	@org.junit.Test
	public void test2(){
//		BaseUtil.out(new Power("aa").equals("aa"));
//		BaseUtil.out(new Power("aa").hashCode() == "aa".hashCode());
////		BaseUtil.out("/BeyondStar/admin/admin/adminPost.jsp".replaceFirst("/BeyondStar", ""));
//		BaseUtil.out(BaseDao.get(Role.class, "12433214").getPowerSet().contains("设置系统"));
//		BaseUtil.out(BaseDao.get(People.class, "admin").getRoleSet().contains("超级管理员"));
		Set<Power> p = new HashSet<Power>();
		p.add(new Power("aa"));
//		new HashSet<E>().contains(o);
		Map<Power, String> map = new HashMap<Power, String>();
		map.put(new Power("aa"), "fds");
		BaseUtil.out(p.contains("aa"));
		BaseUtil.out(map.containsKey("aa"));
	}
	
	@org.junit.Test
	public void t(){
		try {
//			Session s;
			BaseUtil.out(PathUtil.getClassesPath());
			HibernateUtil.getSession();
			HibernateUtil.closeSession();
		} catch (Error e) {
			e.printStackTrace();
		}
	}
	
	
	@org.junit.Test
	public void test52(){
		String temp = HttpUtil.getRequestString(HttpUtil.http_post, "http://192.168.1.103:5568", null, null, "");
		BaseUtil.out(temp+"yy");
		TaskUtil.startTask(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println(Thread.currentThread().getId());
			}
		}, 1, TimeUnit.SECONDS);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("dd"+Thread.currentThread().getId());
	}
	
	@org.junit.Test
	public void testJsoup(){
		try {
			BaseUtil.out(AESUtil.aesEncode(Md5Util.Md5Digest("123456")));
			BaseUtil.out(Md5Util.Md5Digest(StringUtil.getSecureRandomString()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@org.junit.Test
	public void testGson(){
		String url = WechatConstantParamter.pullUserInformationUrl
				.replaceFirst("#PAGE_ACCESS_TOKEN#", "ff")
				.replaceFirst("#OPENID#", "ff").replaceFirst("#LANG#", "ff");
		BaseUtil.out(url);
	}
	
	@org.junit.Test
	public void test3(){
		List<Menu> temp = DaoUtil.listAllObj(Menu.class);
		List<Menu> menuList = new ArrayList<Menu>();
		
		for(Menu m:temp){
			menuList.add(DomainUtil.copyDomain(m));
		}
		BaseUtil.outJson(temp);
		BaseUtil.outJson(menuList);
	}
	@org.junit.Test
	public void test37(){
		try {
			UtilsUtil.initUtils();
			Configuration configuration = new Configuration().configure();
			StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
			.applySettings(configuration.getProperties());
			configuration.buildSessionFactory(builder.build());
		} catch (Exception e) {
			e.printStackTrace();
		}catch (Error e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		//不应该给null值
		FileInputStream fis = null;
		FileOutputStream fos = null;

		try {
			fis = new FileInputStream("d:/办公学习/javatest/To.txt");
			fos = new FileOutputStream("d:/办公学习/javatest/From.txt");
			byte[] buffer = new byte[1024];
			int temp;
			while ((temp = fis.read(buffer, 0, buffer.length)) != -1) {
				System.out.println(temp);
				// fos.write(buffer,0,buffer.length);改为下面这个
				fos.write(buffer, 0, temp);
			}
			fos.flush();
		} catch (Exception e) {
			//不是这样写的,应该是下面这样System.out.println(e);
			e.printStackTrace();
		} finally {
			try {
				fis.close();
				fos.close();
			} catch (Exception e) {
				//System.out.println(e);
				e.printStackTrace();
			}
		}
	}
	@org.junit.Test
	public void test44(){
		WechatUser obj = new WechatUser();
		obj.setOpenid("eee");
		obj.setCity("ffd");
//		System.out.println(DaoUtil.saveOrUpdate(new People("99")));
		DomainUtil.FuzzyQueryInitialize(obj);
		BaseUtil.outJson(obj);
	}
	
	@org.junit.Test
	public void test(){
		Information information = new Information();
		information.setAuthor(new User("admin"));
		Programa programa = new Programa();
		programa.setName("新闻");
		information.setPrograma(programa);
		information.setPublishDate_max_1(new Date());
		information.setPublishDate_min_1(new Date());
		information.setTitle("fdf");
		TestEfficiency test = new TestEfficiency();
		DaoUtil.getSelectHql(information,null,null);
//		DaoUtil.listAllObj(information);
		test.begin();
		for (int i = 0; i < 1000; i++) {
//			DaoUtil.listAllObj(information);
			information.setAuthor(new User(String.format("admin32%d", i)));
			programa.setName(String.format("新闻423%d", i));
			information.setPrograma(programa);
			information.setPublishDate_max_1(new Date());
			information.setPublishDate_min_1(new Date());
			information.setTitle(String.format("新闻dsad3%d", i));
			DaoUtil.getSelectHql(information,null,null);
		}
			test.endAndOutUsedTime("用时");
		
	}
}