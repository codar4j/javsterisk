package com.web.javsterisk.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

import com.web.javsterisk.entity.SipBuddies;
import com.web.javsterisk.util.HibernateUtil;

import java.util.List;

/**
 * 
 * @author freddycucho
 *
 */
public class SipBuddiesDAO {
	
	private static final Logger log = LogManager.getLogger(SipBuddiesDAO.class);

	/**
	 * Find record by id
	 * @param id
	 * @return SipBuddies
	 */
	public SipBuddies findById(Long id) {
		log.info("Find by id : {}", id);	
		SipBuddies sipBuddies = null;
		try {
			HibernateUtil.openSessionAndBindToThread();
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			sipBuddies = session.get(SipBuddies.class, id);
		} finally {
			HibernateUtil.closeSessionAndUnbindFromThread();	
		}		
		return sipBuddies;
	}
	
	/**
	 * List all records ordered by id
	 * @return List<SipBuddies>
	 */
	public List<SipBuddies> findAllOrderedById() {
		log.info("Listing all records");
		List<SipBuddies> list = null;
		try {
			HibernateUtil.openSessionAndBindToThread();
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			Criteria criteria = session.createCriteria(SipBuddies.class);
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
	 * @throws SipBuddies
	 */
	public void register(SipBuddies object) throws Exception {
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
			throw e;
		} finally {
			HibernateUtil.closeSessionAndUnbindFromThread();	
		}
	}

	/**
	 * Modifier record in DB
	 * @param object
	 * @throws SipBuddies
	 */
	public void modifier(SipBuddies object) throws Exception {			
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
	 * @throws SipBuddies
	 */
	public void deleter(SipBuddies object) throws Exception {		
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
