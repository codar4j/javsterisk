package com.web.javsterisk.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

import com.web.javsterisk.entity.Extensions;
import com.web.javsterisk.util.HibernateUtil;

import java.util.List;

/**
 * 
 * @author freddycucho
 *
 */
public class ExtensionsDAO {
	
	private static final Logger log = LogManager.getLogger(ExtensionsDAO.class);

	/**
	 * Find record by id
	 * @param id
	 * @return Extensions
	 */
	public Extensions findById(Long id) {
		log.info("Find by id : {}", id);
		Extensions ext = null;
		try {
			HibernateUtil.openSessionAndBindToThread();
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			ext = session.get(Extensions.class, id);
		} finally {
			HibernateUtil.closeSessionAndUnbindFromThread();	
		}	
		return ext;
	}
	
	/**
	 * List all records ordered by id
	 * @return List<Extensions>
	 */
	public List<Extensions> findAllOrderedById() {
		log.info("Listing all records");
		List<Extensions> list = null;
		try {
			HibernateUtil.openSessionAndBindToThread();
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			Criteria criteria = session.createCriteria(Extensions.class);
			criteria.addOrder(Order.asc("id"));
			list = criteria.list();
		} finally {
			HibernateUtil.closeSessionAndUnbindFromThread();	
		}		
		return list;		
	}
	
	/**
	 * Register record in DB
	 * @param object
	 * @throws Exceptions
	 */
	public void register(Extensions object) throws Exception {
		log.info("Register : {}", object.toString());
		Transaction tx = null;
		try {
			HibernateUtil.openSessionAndBindToThread();			
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.save(object);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			log.error(e.getMessage(), e);				
		} finally {
			HibernateUtil.closeSessionAndUnbindFromThread();	
		}	
	}

	/**
	 * Modifier record in DB
	 * @param object
	 * @throws Exceptions
	 */
	public void modifier(Extensions object) throws Exception {			
		log.info("Modifier : {}", object.toString());
		Transaction tx = null;
		try {
			HibernateUtil.openSessionAndBindToThread();			
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.update(object);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			log.error(e.getMessage(), e);				
		} finally {
			HibernateUtil.closeSessionAndUnbindFromThread();	
		}
	}

	/**
	 * Deleter record in DB
	 * @param object
	 * @throws Exceptions
	 */
	public void deleter(Extensions object) throws Exception {		
		log.info("Deleter : {}", object.toString());
		Transaction tx = null;
		try {
			HibernateUtil.openSessionAndBindToThread();			
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			session.delete(object);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			log.error(e.getMessage(), e);				
		} finally {
			HibernateUtil.closeSessionAndUnbindFromThread();	
		}
	}
	
}
