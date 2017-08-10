package com.web.javsterisk.dao;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.web.javsterisk.entity.Parameter;
import com.web.javsterisk.util.HibernateUtil;

import java.util.List;

/**
 * 
 * @author freddycucho
 *
 */
public class ParameterDAO {

	private static final Logger log = LogManager.getLogger(ParameterDAO.class);
	
	/**
	 * Find record by id
	 * @param id
	 * @return Parameter
	 */
	public Parameter findById(Long id) {
		log.info("Find by id : {}", id);	
		Parameter parameter = null;
		try {
			HibernateUtil.openSessionAndBindToThread();
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			parameter = session.get(Parameter.class, id);
		} finally {
			HibernateUtil.closeSessionAndUnbindFromThread();	
		}		
		return parameter;
	}

	/**
	 * Find record by name
	 * @param name
	 * @return Parameter
	 */
	public Parameter findByName(String name) {
		log.info("Find by name : {}", name);
		Parameter paramter = null;
		try {
			HibernateUtil.openSessionAndBindToThread();
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			Criteria criteria = session.createCriteria(Parameter.class);
			criteria.add(Restrictions.eq("name", name));
			paramter = criteria.list().size() > 0 ? (Parameter)criteria.list().get(0) : null;
		} finally {
			HibernateUtil.closeSessionAndUnbindFromThread();	
		}		
		return paramter;		
	}
	
	/**
	 * List all records
	 * @return List<Parameter>
	 */
	public List<Parameter> findAllOrderedByName() {
		log.info("Listing all records");
		List<Parameter> list = null;
		try {
			HibernateUtil.openSessionAndBindToThread();
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			Criteria criteria = session.createCriteria(Parameter.class);
			criteria.addOrder(Order.desc("name"));
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
	public void register(Parameter object) throws Exception {
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
	public void modifier(Parameter object) throws Exception {			
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
	public void deleter(Parameter object) throws Exception {		
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
