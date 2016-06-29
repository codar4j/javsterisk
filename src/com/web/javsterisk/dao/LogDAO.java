package com.web.javsterisk.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.web.javsterisk.entity.Log;
import com.web.javsterisk.util.HibernateUtil;

import java.util.List;

/**
 * 
 * @author freddycucho
 *
 */
public class LogDAO {
	
	private static final Logger log = LogManager.getLogger(LogDAO.class);

		/**
	 * Find record by id
	 * @param id
	 * @return Log
	 */
	public Log findById(Long id) {
		log.info("Find by id : {}", id);
		Log log = null;
		try {
			HibernateUtil.openSessionAndBindToThread();
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			log = session.get(Log.class, id);
		} finally {
			HibernateUtil.closeSessionAndUnbindFromThread();	
		}		
		return log;			
	}

	/**
	 * List all records
	 * @return List<Log>
	 */
	public List<Log> findAllRecordsOrderedByTimestmp() {
		log.info("Listing all");
		List<Log> list = null;
		try {
			HibernateUtil.openSessionAndBindToThread();
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			Criteria criteria = session.createCriteria(Log.class);
			criteria.addOrder(Order.desc("eventDate"));
			list = criteria.list();
		} finally {
			HibernateUtil.closeSessionAndUnbindFromThread();	
		}		
		return list;		
	}
	
	/**
	 * List all by date (the date is in long)
	 * @param since
	 * @param to
	 * @return List<Log>
	 */
	public List<Log> findAllRecordsFilteredByDate(Long since, Long to) {
		log.info("Listing all by date");
		List<Log> list = null;
		try {
			HibernateUtil.openSessionAndBindToThread();
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			Criteria criteria = session.createCriteria(Log.class);
			criteria.add(Restrictions.between("eventDate", since, to));
			criteria.addOrder(Order.desc("eventDate"));
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
	public void register(Log object) throws Exception {
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
	public void modifier(Log object) throws Exception {
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
	public void deleter(Log object) throws Exception {		
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