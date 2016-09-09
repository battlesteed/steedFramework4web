package steed.test;


import org.junit.Test;

import steed.domain.system.Property;
import steed.ext.domain.terminal.TerminalUser;
import steed.ext.domain.user.User;
import steed.util.base.test.TestEfficiency;
import steed.util.dao.DaoUtil;
import steed.util.dao.HibernateUtil;
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
