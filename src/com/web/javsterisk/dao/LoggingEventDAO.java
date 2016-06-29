package com.web.javsterisk.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.web.javsterisk.entity.LoggingEvent;
import com.web.javsterisk.util.HibernateUtil;

import java.util.List;

/**
 * 
 * @author freddycucho
 *
 */
public class LoggingEventDAO {
	
	private static final Logger log = LogManager.getLogger(LoggingEventDAO.class);

		/**
	 * Find record by id
	 * @param id
	 * @return LoggingEvent
	 */
	public LoggingEvent findById(Long id) {
		log.info("Find by id : {}", id);
		LoggingEvent loggingEvent = null;
		try {
			HibernateUtil.openSessionAndBindToThread();
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			loggingEvent = session.get(LoggingEvent.class, id);
			log.info("Getting LogginEventProperties by id : {} - size : {}", id, loggingEvent.getLoggingEventProperties().size());
			log.info("Getting LogginEventExceptions by id : {} - size : {}", id, loggingEvent.getLoggingEventExceptions().size());
		} finally {
			HibernateUtil.closeSessionAndUnbindFromThread();	
		}		
		return loggingEvent;			
	}

	/**
	 * List all records
	 * @return List<LoggingEvent>
	 */
	public List<LoggingEvent> findAllRecordsOrderedByTimestmp() {
		log.info("Listing all");
		List<LoggingEvent> list = null;
		try {
			HibernateUtil.openSessionAndBindToThread();
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			Criteria criteria = session.createCriteria(LoggingEvent.class);
			criteria.addOrder(Order.desc("timestmp"));
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
	 * @return List<LoggingEvent>
	 */
	public List<LoggingEvent> findAllRecordsFilteredByDate(Long since, Long to) {
		log.info("Listing all by date");
		List<LoggingEvent> list = null;
		try {
			HibernateUtil.openSessionAndBindToThread();
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			Criteria criteria = session.createCriteria(LoggingEvent.class);
			criteria.add(Restrictions.between("timestmp", since, to));
			criteria.addOrder(Order.desc("timestmp"));
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
	public void register(LoggingEvent object) throws Exception {
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
	public void modifier(LoggingEvent object) throws Exception {
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
	public void deleter(LoggingEvent object) throws Exception {		
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