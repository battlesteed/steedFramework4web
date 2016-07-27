package steed.test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.hql.internal.ast.QueryTranslatorImpl;
import org.hibernate.hql.spi.QueryTranslator;
import org.hibernate.transform.AliasedTupleSubsetResultTransformer;
import org.junit.Test;

import steed.ext.domain.user.User;
import steed.util.base.BaseUtil;
import steed.util.dao.DaoUtil;
import steed.util.dao.HibernateUtil;

public class HibernateTest {
	@Test
	public void testSonQuery(){
		Session s = null;
		try {
			s = HibernateUtil.getSession();
			String hql = "from People p where p.nickName "
					+ "in (select d.nickName from People d where d.nickName='admin') ORDER BY RAND()";
			BaseUtil.outJson(s.createQuery(hql).list());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (s != null) {
				s.close();
			}
		}
	}
	@Test
	public void testRand(){
		Session s = null;
		try {
			s = HibernateUtil.getSession();
			String hql = "from People p ORDER BY RAND()";
			Query createQuery = s.createQuery(hql);
			createQuery.setMaxResults(1);
			createQuery.setFirstResult(1);
			BaseUtil.out(createQuery.list());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (s != null) {
				s.close();
			}
		}
	}
	@Test
	public void testUpdate(){
		Session s = null;
		try {
			s = HibernateUtil.getSession();
			String hql = "update People p set p.sex=:sex,p.name=:name where p.nickName='admin' ";
			Query q = s.createQuery(hql);
			q.setParameter("sex", "男");
			q.setParameter("name", "战马");
			BaseUtil.outJson(q.executeUpdate());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (s != null) {
				s.close();
			}
		}
	}
	@Test
	public void testUpdateByquery(){
		Session s = null;
		try {
			s = HibernateUtil.getSession();
			User people = new User("admin");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("sex", "男");
			map.put("name", "战马");
			BaseUtil.outJson(DaoUtil.updateByQuery(people, map));
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (s != null) {
				s.close();
			}
		}
	}
	
	@Test
	public void testSqlQuery(){
		String sql = "select * from People p where 2 = :in";
//		QueryTranslator queryTranslator=new QueryTranslatorImpl(hql,hql,Collections.EMPTY_MAP,(SessionFactoryImplementor) HibernateUtil.getFactory());
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("name", "name1");
//		queryTranslator.compile(map, false);
//		BaseUtil.out();
		SQLQuery createSQLQuery = HibernateUtil.getSession().createSQLQuery(sql);
		createSQLQuery.setParameter("in", 1);
		BaseUtil.outJson(createSQLQuery.addEntity(User.class).list());;
	}
	@Test
	public void testSqlMap(){
		String sql = "select * from People p ";
//		QueryTranslator queryTranslator=new QueryTranslatorImpl(hql,hql,Collections.EMPTY_MAP,(SessionFactoryImplementor) HibernateUtil.getFactory());
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("name", "name1");
//		queryTranslator.compile(map, false);
//		BaseUtil.out();
		SQLQuery query = HibernateUtil.getSession().createSQLQuery(sql);
//		BaseUtil.outJson(query.addEntity(People.class).list());
		AliasedTupleSubsetResultTransformer f = new AliasedTupleSubsetResultTransformer() {

			@Override
			public boolean isTransformedValueATupleElement(String[] arg0,
					int arg1) {

				return false;
			}

			@Override
			// 重写这个方法是关键
			public Object transformTuple(Object[] tuple/* 值数组 */,
					String[] aliases/* 字段数组 */) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 0; i < aliases.length; i++) {

					map.put(aliases[i].toLowerCase(), tuple[i]);

				}
				return map;
			}
		};
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> resultList = query.setResultTransformer(f).list();
	}
	
	@Test
	public void testHql2Sql(){
		String hql = "select p.name from People p where 1 > (select d.name from p.roleSet d where d.name='超级管理员')";
		QueryTranslator queryTranslator=new QueryTranslatorImpl(hql,hql,Collections.EMPTY_MAP,(SessionFactoryImplementor) HibernateUtil.getFactory());
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", "name1");
		queryTranslator.compile(map, false);
		BaseUtil.out(queryTranslator.getSQLString());
	}
	@Test
	public void testDaoUtil(){
		//BaseUtil.outJson(DaoUtil.listByKeys(Matter.class, "53,54"));
	}
}
