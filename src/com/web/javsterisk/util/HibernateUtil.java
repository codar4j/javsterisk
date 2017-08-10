package com.web.javsterisk.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.context.internal.ThreadLocalSessionContext;

/**
 * 
 * @author freddycucho
 * 23-03-2015 4:00:50
 */
public class HibernateUtil {
	
	private static final Logger log = LogManager.getLogger(HibernateUtil.class);
	
	private static SessionFactory sessionFactory;// = buildSessionFactory();
	
	public static synchronized void buildSessionFactory() {
		 if(sessionFactory == null) {
			 	//Configuration cfg = new Configuration().configure();
			 	StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure().build();//.applySettings(cfg.getProperties()).build();
			try {
			 	sessionFactory = new MetadataSources( serviceRegistry ).buildMetadata().buildSessionFactory();//cfg.buildSessionFactory(serviceRegistry);
			 	log.debug("Initial SessionFactory creation successfully.");
	        }
	        catch (Throwable ex) {
	        	log.error("Initial SessionFactory creation failed. {}", ex);
	        	StandardServiceRegistryBuilder.destroy(serviceRegistry);
	            throw new ExceptionInInitializerError(ex);	            
	        }
		 }
	}

	public static SessionFactory getSessionFactory() {  
		if (sessionFactory == null)  {
			buildSessionFactory();
		}
		return sessionFactory;  
	}

	public static void openSessionAndBindToThread() {
		Session session = sessionFactory.openSession();
		ThreadLocalSessionContext.bind(session);
	}

	public static void closeSessionAndUnbindFromThread() {
		Session session = ThreadLocalSessionContext.unbind(sessionFactory);
		if (session!=null) {
			session.close();
		}
	}

	public static void closeSessionFactory() {
		if ((sessionFactory!=null) && (sessionFactory.isClosed()==false)) {
			sessionFactory.close();
		}
	}

}
