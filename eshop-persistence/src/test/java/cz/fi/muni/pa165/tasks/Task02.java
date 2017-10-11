package cz.fi.muni.pa165.tasks;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.validation.ConstraintViolationException;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cz.fi.muni.pa165.PersistenceSampleApplicationContext;
import cz.fi.muni.pa165.entity.Category;
import cz.fi.muni.pa165.entity.Product;

@ContextConfiguration(classes = PersistenceSampleApplicationContext.class)
public class Task02 extends AbstractTestNGSpringContextTests {

    @PersistenceUnit
    private EntityManagerFactory emf;
    private Category kitchen = new Category();
    private Category electro = new Category();
    private Product flashlight = new Product();
    private Product kitchenRobot = new Product();
    private Product plate = new Product();

    @BeforeClass
    public void createData() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        electro.setName("electro");
        em.persist(electro);
        kitchen.setName("kitchen");
        em.persist(kitchen);
        flashlight.setName("flashlight");
        flashlight.addCategory(electro);
        em.persist(flashlight);
        kitchenRobot.setName("kitchenRobot");
        kitchenRobot.addCategory(electro);
        kitchenRobot.addCategory(kitchen);
        em.persist(kitchenRobot);
        plate.setName("plate");
        plate.addCategory(kitchen);
        em.persist(plate);
        em.getTransaction().commit();
        em.close();
    }

    private void assertContainsCategoryWithName(Set<Category> categories,
            String expectedCategoryName) {
        for (Category cat : categories) {
            if (cat.getName().equals(expectedCategoryName)) {
                return;
            }
        }

        Assert.fail("Couldn't find category " + expectedCategoryName + " in collection " + categories);
    }

    private void assertContainsProductWithName(Set<Product> products,
            String expectedProductName) {

        for (Product prod : products) {
            if (prod.getName().equals(expectedProductName)) {
                return;
            }
        }

        Assert.fail("Couldn't find product " + expectedProductName + " in collection " + products);
    }

    @Test
    public void testFlashlight() {
        EntityManager em = emf.createEntityManager();

        Product prod = em.find(Product.class, flashlight.getId());
        Assert.assertEquals(prod.getCategories().size(), 1);
        Assert.assertEquals(prod.getCategories().iterator().next().getName(), "electro");

        em.close();
    }

    @Test
    public void testPlate() {
        EntityManager em = emf.createEntityManager();

        Product prod = em.find(Product.class, plate.getId());
        Assert.assertEquals(prod.getCategories().size(), 1);
        Assert.assertEquals(prod.getCategories().iterator().next().getName(), "kitchen");

        em.close();
    }

    @Test
    public void testKitchenRobot() {
        EntityManager em = emf.createEntityManager();

        Product prod = em.find(Product.class, kitchenRobot.getId());
        Assert.assertEquals(prod.getCategories().size(), 2);
        assertContainsCategoryWithName(prod.getCategories(), "electro");
        assertContainsCategoryWithName(prod.getCategories(), "kitchen");

        em.close();
    }

    @Test
    public void testKitchen() {
        EntityManager em = emf.createEntityManager();

        Category cat = em.find(Category.class, kitchen.getId());
        Assert.assertEquals(cat.getProducts().size(), 2);
        assertContainsProductWithName(cat.getProducts(), "kitchenRobot");
        assertContainsProductWithName(cat.getProducts(), "plate");

        em.close();
    }

    @Test
    public void testElectro() {
        EntityManager em = emf.createEntityManager();

        Category cat = em.find(Category.class, electro.getId());
        Assert.assertEquals(cat.getProducts().size(), 2);
        assertContainsProductWithName(cat.getProducts(), "flashlight");
        assertContainsProductWithName(cat.getProducts(), "kitchenRobot");

        em.close();
    }
}