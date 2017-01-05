package com.web.javsterisk.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

import com.web.javsterisk.entity.Context;
import com.web.javsterisk.util.HibernateUtil;

import java.util.List;

/**
 * 
 * @author freddycucho
 *
 */
public class ContextDAO {
	
	private static final Logger log = LogManager.getLogger(ContextDAO.class);

	/**
	 * Find record by id
	 * @param id
	 * @return Context
	 */
	public Context findById(Long id) {
		log.info("Find by id : {}", id);
		Context context = null;
		try {
			HibernateUtil.openSessionAndBindToThread();
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			context = session.get(Context.class, id);
		} finally {
			HibernateUtil.closeSessionAndUnbindFromThread();	
		}	
		return context;
	}
	
	/**
	 * List all records ordered by id
	 * @return List<Context>
	 */
	public List<Context> findAllOrderedById() {
		log.info("Listing all records");
		List<Context> list = null;
		try {
			HibernateUtil.openSessionAndBindToThread();
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			Criteria criteria = session.createCriteria(Context.class);
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
	public void register(Context object) throws Exception {
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
	public void modifier(Context object) throws Exception {			
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
	public void deleter(Context object) throws Exception {		
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
