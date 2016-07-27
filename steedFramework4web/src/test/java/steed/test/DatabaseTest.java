package steed.test;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Query;
import org.junit.Test;

import steed.ext.domain.system.Config;
import steed.ext.domain.system.Menu;
import steed.ext.domain.user.User;
import steed.ext.domain.user.Role;
import steed.util.base.BaseUtil;
import steed.util.base.test.TestEfficiency;
import steed.util.dao.DaoUtil;
import steed.util.dao.HibernateUtil;

public class DatabaseTest{
	@Test
	public void test2(){
		Pattern p = Pattern.compile("\\#+([^#]+)\\#+",Pattern.UNICODE_CASE);
		StringBuffer sb = new StringBuffer("##########验证码#############");
		Matcher m = p.matcher(sb);
        while (m.find(0)) {
        	BaseUtil.out(m.groupCount());
            BaseUtil.out(m.group());
            BaseUtil.out(m.group(1));
			sb.replace(m.start(), m.end(), "验证码");
        }
        BaseUtil.out(sb.toString());
	}
	@Test
	public void test8(){
//		BaseUtil.out(DaoUtil.getSelectHql(domain, desc, asc));
	}
	@Test
	public void test5(){
		try {
			String hql = "from cn.com.beyondstar.domain.wechat.WechatAccount Article_0 where 1=1 and Article_0.user_nickName like :title";
//			String hql = "from Article Article_0 where 1=1 and Article_0.title like :title";
			Query q = HibernateUtil.getSession().createQuery(hql);
			q.setParameter("title", "%dd%");
			BaseUtil.outJson(q.list());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}
	}

	@Test
	public void test7(){
		try {
			BaseUtil.outJson(DaoUtil.get(User.class, "admin"));
//			BaseUtil.outJson(BaseDao.get(Address.class, 110105));
			TestEfficiency efficiency = new TestEfficiency();
			efficiency.begin();
			for (int i = 0; i < 6000; i++) {
				DaoUtil.listAllObj(Menu.class);
			}
			efficiency.endAndOutUsedTime(null);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}
	}
	@Test
	public void test76(){
		User load = DaoUtil.load(User.class, "admin");
		BaseUtil.out(load.getPhoneNumber());
		load.setPhoneNumber(3663+"");
		load.update();
	}
	
	
	
	@Test
	public void test9(){
		try {
			Set<Role> roleSet = new HashSet<Role>();
			Set<Role> roleSet2 = new HashSet<Role>();
			roleSet2.add(new Role("管理员"));
			roleSet.add(new Role("超级管理员"));
			
			List<String> nickNameList = new ArrayList<String>();
			nickNameList.add("admin");
			User p = new User();
			p.setRoleSet(roleSet2);
			p.setRoleSet_not_in_1(roleSet);
			BaseUtil.outJson(DaoUtil.listObj(15, 1, p, null, null));
//			BaseUtil.outJson(DaoUtil.listObj(1, p, null, null));
		} catch (Exception e) {
			BaseUtil.out(e);
		}
	}
	@Test
	public void test4(){
		try {
			String hql = "select p from People p,Role r where r in (:nameList) ";
//			String hql = "from steed.domain.people.People p right join p.roleSet r where r in (:nameList) ";
			Query q = HibernateUtil.getSession().createQuery(hql);
			Set<Role> roleSet = new HashSet<Role>();
			roleSet.add(new Role("管理员"));
			List<Role> list = new ArrayList<Role>();
			list.add(new Role("管理员"));
			list.add(new Role("超级管理员"));
			
			q.setParameterList("nameList", list);
			List<User> list2 = q.list();
			for (User p:list2) {
				p.initializeAll();
			}
			BaseUtil.outJson(list2);
		} catch (Exception e) {
			BaseUtil.out(e);
		}
	}
	@Test
	public void test11(){
		try {
			String hql = "update steed.domain.people.People p set p.description=? where p=?";
			HibernateUtil.getSession().beginTransaction();
			Query q = HibernateUtil.getSession().createQuery(hql);
//			q.setParameter("description1", "description");
			q.setParameter(0, "pp1111ppp");
			q.setParameter(1, new User("admin"));
			q.executeUpdate();
//			q.setParameter("nickName", "admin");
			HibernateUtil.getSession().getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void test12(){
		try {
			TestEfficiency efficiency = new TestEfficiency();
			HibernateUtil.getSession();
			efficiency.begin();
			
			for (int i = 0; i < 1000; i++) {
				Config c = new Config();
				c.setKee(i+"");
				c.setValue(""+i);
				DaoUtil.save(c);
			}
			HibernateUtil.closeSession();
			efficiency.endAndOutUsedTime("插入用时");
			
			efficiency.begin();
			for (int i = 0; i < 1000; i++) {
//				c.setKee(i+"");
//				c.setValue(""+i);
				DaoUtil.get(Config.class, i+"");
			}
			HibernateUtil.closeSession();
			efficiency.endAndOutUsedTime("查询用时");
			
			efficiency.begin();
			for (int i = 0; i < 1000; i++) {
				Config c = new Config();
				c.setKee(i+"");
				c.setValue(""+i);
				DaoUtil.delete(c);
			}
			HibernateUtil.closeSession();
			efficiency.endAndOutUsedTime("删除用时");
//			DaoUtil.listAllObj(Menu.class);
			
		} catch (Exception e) {
			BaseUtil.out(e);
		}
	}
	
	@Test
	public void testDatabase(){
		Menu menu = new Menu();
		menu.setId_max_1(-1);
		System.out.println(DaoUtil.deleteByQuery(menu));
	}
}
