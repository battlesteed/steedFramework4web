package steed.test;

import org.junit.Test;

import steed.util.dao.HibernateUtil;

public class A {
	@Test
	public void test1(){
		HibernateUtil.getSession();
	}
}
