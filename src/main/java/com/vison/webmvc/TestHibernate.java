package com.vison.webmvc;

import com.vison.webmvc.entity.User;
import com.vison.webmvc.framework.HibernateLoader;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author vison.cao <visonforcoding@gmail.com>
 */
public class TestHibernate {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Transaction transaction = null;
        try (Session session = HibernateLoader.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();
            User user = new User();
            user.setName("vison");
            user.setEmail("visonforcoding@gmail.com");
            user.setCountry("赛德克巴莱");
            // save the student object
            session.save(user);
            // commit transaction
            transaction.commit();
            session.close();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

}
