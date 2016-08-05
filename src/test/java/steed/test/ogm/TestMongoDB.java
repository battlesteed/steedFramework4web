package steed.test.ogm;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import org.junit.Test;

public class TestMongoDB {
	@Test
	public void startUp(){
		TransactionManager tm = com.arjuna.ats.jta.TransactionManager.transactionManager();

		//build the EntityManagerFactory as you would build in in Hibernate ORM
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(
		    "ogm-jpa-tutorial");
		try {
			tm.begin();
		} catch (NotSupportedException e1) {
			e1.printStackTrace();
		} catch (SystemException e1) {
			e1.printStackTrace();
		}
		EntityManager em = emf.createEntityManager();
		Breed collie = new Breed();
		collie.setName("Collie");
//		em.persist(collie);
		Dog dina = new Dog();
		dina.setId(1L);
		dina.setName("Dina");
		em.persist(dina);
		em.flush();
		em.close();
		try {
			tm.commit();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HeuristicMixedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
