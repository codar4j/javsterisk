package com.web.javsterisk.dao;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.web.javsterisk.entity.Cdr;
import com.web.javsterisk.util.HibernateUtil;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author freddycucho
 *
 */
public class CdrDAO {
	
	private static final Logger log = LogManager.getLogger(CdrDAO.class);

	/**
	 * Find record by id
	 * @param id
	 * @return Cdr
	 */
	public Cdr findById(Long id) {
		log.info("Find by id : {}", id);		
		Cdr cdr = null;
		try {
			HibernateUtil.openSessionAndBindToThread();
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			cdr = session.get(Cdr.class, id);
		} finally {
			HibernateUtil.closeSessionAndUnbindFromThread();	
		}		
		return cdr;
	}
	
	/**
	 * List all records ordered by Id
	 * @return List<Cdr>
	 */
	@SuppressWarnings("unchecked")
	public List<Cdr> findAllOrderedById() {
		log.info("Listing all records");
		List<Cdr> list = null;
		try {
			HibernateUtil.openSessionAndBindToThread();
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			Criteria criteria = session.createCriteria(Cdr.class);
			criteria.addOrder(Order.asc("id"));
			list = criteria.list();
		} finally {
			HibernateUtil.closeSessionAndUnbindFromThread();	
		}		
		return list;
	}
	
	/**
	 * List all records by Date ordered by Id
	 * @param since
	 * @param to
	 * @return List<LoggingEvent>
	 */
	@SuppressWarnings("unchecked")
	public List<Cdr> findAllByDateOrderedById(Date since, Date to) {
		log.info("Listing all by date");
		List<Cdr> list = null;
		try {
			HibernateUtil.openSessionAndBindToThread();
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			Criteria criteria = session.createCriteria(Cdr.class);
			criteria.add(Restrictions.between("calldate", since, to));
			criteria.addOrder(Order.desc("id"));
			list = criteria.list();
		} finally {
			HibernateUtil.closeSessionAndUnbindFromThread();	
		}		
		return list;
	}
	
	/**
	 * List all records by Date and extension ordered by Id
	 * @param since
	 * @param to
	 * @param extension
	 * @return List<LoggingEvent>
	 */
	@SuppressWarnings("unchecked")
	public List<Cdr> findAllByDateOrderedById(Date since, Date to, String extension) {
		log.info("Listing all by date and extension");
		List<Cdr> list = null;
		try {
			HibernateUtil.openSessionAndBindToThread();
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			Criteria criteria = session.createCriteria(Cdr.class);
			criteria.add(Restrictions.eq("src", extension));
			criteria.add(Restrictions.between("calldate", since, to));
			criteria.addOrder(Order.desc("id"));
			list = criteria.list();
		} finally {
			HibernateUtil.closeSessionAndUnbindFromThread();	
		}
		return list;
	}
	
	/**
	 * Register record in DB
	 * @param object
	 * @throws Exception
	 */
	public void register(Cdr object) throws Exception {
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
	 * @throws Exception
	 */
	public void modifier(Cdr object) throws Exception {			
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
	 * @throws Exception
	 */
	public void deleter(Cdr object) throws Exception {		
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
