package cz.fi.muni.pa165.tasks;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import cz.fi.muni.pa165.PersistenceSampleApplicationContext;
import cz.fi.muni.pa165.entity.Category;


@ContextConfiguration(classes = PersistenceSampleApplicationContext.class)
public class Task01 extends AbstractTestNGSpringContextTests {

	
	@PersistenceUnit
	private EntityManagerFactory emf;

	@Test
	public void categoryTest() {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Category cat = new Category();
		cat.setName("Test");
		em.persist(cat);
		em.getTransaction().commit();
		em.close();
		EntityManager em2 = emf.createEntityManager();
		Category cat2;
                long pk = cat.getId();
		cat2 = em2.find(Category.class, pk);
                Assert.assertEquals("Test", cat2.getName());
	}

}
