package steed.test;


import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import steed.domain.system.Property;
import steed.ext.domain.information.Information;
import steed.ext.domain.information.Programa;
import steed.ext.domain.terminal.TerminalUser;
import steed.ext.domain.user.User;
import steed.hibernatemaster.util.DaoUtil;
import steed.hibernatemaster.util.HibernateUtil;
import steed.hibernatemaster.util.SimpleHqlGenerator;
import steed.util.base.BaseUtil;
import steed.util.base.test.TestEfficiency;
import steed.util.digest.AESUtil;
import steed.util.digest.Md5Util;

public class DatabaseTest{
	/**
	 * 初始修改aes密钥时请运行该例子.
	 */
	@Test
	public void aesInit(){
		User user = DaoUtil.get(User.class, "admin");
		user.setPassword(AESUtil.aesEncode(Md5Util.Md5Digest("123456")));
		user.update();
		
		TerminalUser terminalUser = DaoUtil.get(TerminalUser.class, "admin");
		terminalUser.setPassword(AESUtil.aesEncode("123456"));
		terminalUser.update();
		DaoUtil.managTransaction();
	}
	
	/**
	 * 自定义hql生成器的例子,这里我们要查找姓名或者昵称为admin的用户,
	 * 如果同时把user实体类的姓名和昵称设置为admin的话,系统默认hql生成器会生成姓名和昵称都是admin这样的查询条件,所以我们要自定义hql生成器
	 * 
	 */
	@Test
	public void testHqlGenter(){
		User user = new User();
		user.setName("admin");
		//调用setPersonalHqlGenerator即可设置该对象的个性化hql生成器
		user.setPersonalHqlGenerator(new SimpleHqlGenerator(){
			@Override
			protected void appendSingleWhereCondition(String domainSimpleName, StringBuffer hql,
					List<String> removedEntry, Map<String, ?> query, Entry<String, Object> e,
					Map<String, Object> put) {
				//如果查询字段名不是name的话则调用父类方法生成hql
				if ("name".equals(e.getKey())) {
					hql.append(" and ( ").append(domainSimpleName).append(".name = :name")
						.append(" or ").append(domainSimpleName).append(".nickName = :nickName ) ");
					//这里因为我们多加了一个:nickName参数,所以要调用下面的代码把它加到查询参数里面
					put.put("nickName", e.getValue());
				}else{
					super.appendSingleWhereCondition(domainSimpleName, hql, removedEntry, query, e, put);
				}
			}
			
		});
		
		//这里调用listAllObj生成hql查询数据库,大家可以看一下控制台打印的hql是什么样的.
		BaseUtil.out(DaoUtil.listAllObj(user));
	}
	
	@Test
	public void testListOne(){
		Information information = new Information();
//		information.setPublishDate(new Date());
		BaseUtil.out(DaoUtil.listOne(information));
	}
	
	
	@Test
	public void testFlush(){
		DaoUtil.setAutoManagTransaction(false);
		User user = DaoUtil.get(User.class, "admin");
		DaoUtil.managTransaction();
		DaoUtil.relese();
		HibernateUtil.release();
		
		DaoUtil.setAutoManagTransaction(false);
		user.setName("ad444444");
		user.setNickName("994448888");
		user.save();
		BaseUtil.out(DaoUtil.listOne(user));
		DaoUtil.rollbackTransaction();
	}
	
	@Test
	public void testInsert(){
		TestEfficiency testEfficiency = new TestEfficiency();
		testEfficiency.begin();
		for (int i = 0; i < 1000; i++) {
			Property property = new Property();
			property.setKee(i+"");
			property.setPropertyType(i+"");
			property.save();
		}
		testEfficiency.endAndOutUsedTime("结束");
	}
	
	@Test
	public void testInsert2(){
		TestEfficiency testEfficiency = new TestEfficiency();
		HibernateUtil.getSession();
		HibernateUtil.release();
		DaoUtil.setAutoManagTransaction(false);
		testEfficiency.begin();
		for (int i = 0; i < 10000; i++) {
			Property property = new Property();
			property.setKee(i+"");
			property.setPropertyType(i+"");
			property.save();
		}
		DaoUtil.managTransaction();
		testEfficiency.endAndOutUsedTime("结束");
	}
	
	@Test
	public void testDelete(){
		TestEfficiency testEfficiency = new TestEfficiency();
		testEfficiency.begin();
		for (int i = 0; i < 1000; i++) {
			Property property = new Property();
			property.setKee(i+"");
			property.setPropertyType(i+"");
			property.delete();
		}
		testEfficiency.endAndOutUsedTime("结束");
	}
	
	@Test
	public void testUpdateNotNullFile(){
		Programa programa = new Programa();
		programa.setName("e");
		Information information = new Information();
		information.setId(1);
		information.setPrograma(programa);
		information.updateNotNullField(null);
	}
	
	
	@Test
	public void testDelete2(){
		TestEfficiency testEfficiency = new TestEfficiency();
		HibernateUtil.getSession();
		HibernateUtil.closeSession();
		DaoUtil.setAutoManagTransaction(false);
		testEfficiency.begin();
		for (int i = 0; i < 1000; i++) {
			Property property = new Property();
			property.setKee(i+"");
			property.setPropertyType(i+"");
			property.delete();
		}
		DaoUtil.managTransaction();
		testEfficiency.endAndOutUsedTime("结束");
	}
}
