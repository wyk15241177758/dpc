package com.jt.test.hibernatetest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactory {
     private static Log log = LogFactory.getLog(HibernateSessionFactory.class);
     
     // Path of configuration file
     private static String CONFIG_FILE_LOCATION = "/hibernate.cfg.xml";
     private static String configFile = CONFIG_FILE_LOCATION;

     // Use ThreadLocal to control Session object
     private static final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
     private static Configuration configuration = new Configuration();
     private static org.hibernate.SessionFactory sessionFactory;

     /**
     * Abstraction: Obtain Session
     */
     public static Session getSession() throws HibernateException {
          Session session = threadLocal.get();

          // Rebulid Session object if there is no session in ThreadLocal
          if (session == null || !session.isOpen()) {
               if (sessionFactory == null) {
                    rebuildSessionFactory();
               }
               // Obtain Session object
               session = (sessionFactory != null) ? sessionFactory.openSession()
                         : null;
               threadLocal.set(session);
          }          
          return session;
     }

     /**
     * Abstract: Build SessionFactory object
     */
     public static void rebuildSessionFactory() {
          try {
               // Initial application using configuration file
               configuration.configure(configFile);
               // Create SessionFactory object according to the configuration
               // Data model can be created in MySQL automatically after execute this method
               sessionFactory = configuration.buildSessionFactory();
          } catch (Exception e) {
               e.printStackTrace();
          }
     }

     /**
     * Abstraction: Close Session object
     */
     public static void closeSession() throws HibernateException
     {
          Session session = (Session) threadLocal.get();
          threadLocal.set(null);
          if (session != null)
          {
               session.close();
          }          
     }
}