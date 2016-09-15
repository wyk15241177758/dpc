package com.jt.test.hibernatetest;

import org.hibernate.Transaction;
import org.hibernate.Session;

public class Test {
public static void main(String[] args) {
	   // Get session
    Session session = HibernateSessionFactory.getSession();
    Transaction tx = null;
    try
    {
         // Begin transaction
         tx = session.beginTransaction();
         
         // Create a Product object and set its property
         Product product = new Product();
         product.setName("Apple");
         
         // Save the object
         session.save(product);
         // Commin
         tx.commit();
    }
    catch (Exception e)
    {
         if (tx != null)
         {
              tx.rollback();
         }
         try
         {
              // Spread the exception
              throw e;
         }
         catch (Exception e2)
         {
              e2.printStackTrace();
         }
    }
    finally
    {
         // Close the session
         session.close();
    }
}
}
