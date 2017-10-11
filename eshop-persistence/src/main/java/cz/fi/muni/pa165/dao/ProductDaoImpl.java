package cz.fi.muni.pa165.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.springframework.stereotype.Repository;

import cz.fi.muni.pa165.entity.Product;

@Repository
public class ProductDaoImpl implements ProductDao {

	@PersistenceContext
	private EntityManager em;

	@Override
	public void create(Product c) {
		em.persist(c);
	}

	@Override
	public List<Product> findAll() {
		return em.createQuery("select c from Product c", Product.class)
				.getResultList();
	}

	@Override
	public Product findById(Long id) {
		return em.find(Product.class, id);
	}

	@Override
	public void remove(Product c) {
		em.remove(c);
	}

	@Override
	public List<Product> findByName(String name) {
		try {
			return em
                            .createQuery("select c from Product c where name = :name",
                                Product.class).setParameter(":name", name).getResultList();
		} catch (NoResultException nrf) {
			return null;
		}
	}

}
