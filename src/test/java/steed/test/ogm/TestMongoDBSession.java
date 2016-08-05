package steed.test.ogm;

import org.hibernate.ogm.OgmSession;
import org.junit.Test;

import steed.largewebsite.ogm.OgmUtil;

public class TestMongoDBSession {
	@Test
	public void startUp(){
		OgmSession session = OgmUtil.getSession();
		Dog dina = session.get(Dog.class, 12L);
//		Dog dina = new Dog();
//		dina.setId(12L);
		dina.setName("Dindddda");
		session.beginTransaction();
//		session.save(dina);
		session.update(dina);
		session.flush();
//		session.
		session.getTransaction().commit();
		session.close();
	}
}
