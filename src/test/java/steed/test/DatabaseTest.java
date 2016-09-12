package steed.test;


import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import steed.domain.system.Property;
import steed.ext.domain.information.Programa;
import steed.ext.domain.terminal.TerminalUser;
import steed.ext.domain.user.User;
import steed.util.base.BaseUtil;
import steed.util.base.test.TestEfficiency;
import steed.util.dao.DaoUtil;
import steed.util.dao.HibernateUtil;
import steed.util.dao.SimpleHqlGenerator;
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
	
	@Test
	public void testOrQuery(){
		Programa programa = new Programa();
		programa.setDescription("ddd");
		programa.setPersonalHqlGenerator(new SimpleHqlGenerator(){

			@Override
			protected void appendSingleWhereCondition(String domainSimpleName, StringBuffer hql,
					List<String> removedEntry, Entry<String, ? extends Object> e, Map<String, ? extends Object> put) {
				if ("description".equals(e.getKey())) {
					hql.append("and "+domainSimpleName+".description is not null ");
					removedEntry.add(e.getKey());
				}else{
					super.appendSingleWhereCondition(domainSimpleName, hql, removedEntry, e, put);
				}
			}
			
		});
		List<Programa> listAllObj = DaoUtil.listAllObj(programa);
		BaseUtil.out(listAllObj.size());
		//and (aa = :aa or bb = :bb) 
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
