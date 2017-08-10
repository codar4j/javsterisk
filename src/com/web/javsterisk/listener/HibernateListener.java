package com.web.javsterisk.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.web.javsterisk.util.HibernateUtil;


/**
 * 
 * @author freddycucho
 * 23-03-2015 4:01:46
 */
public class HibernateListener implements ServletContextListener {
	
	private static final Logger log = LogManager.getLogger(HibernateListener.class);
	
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		log.debug("Hibernate context initialized");
		HibernateUtil.buildSessionFactory();
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		log.debug("Hibernate context destroyed"); 		
		HibernateUtil.closeSessionFactory();
	}

}
