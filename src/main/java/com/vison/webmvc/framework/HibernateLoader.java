package com.vison.webmvc.framework;

import com.vison.webmvc.config.DatabaseConfig;
import com.vison.webmvc.config.Log;
import com.vison.webmvc.entity.User;
import java.io.File;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.relational.Database;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

/**
 *
 * @author vison.cao <visonforcoding@gmail.com>
 */
public class HibernateLoader {

    public static EntityManagerFactory emf;
    public static SessionFactory sessionFactory = null;

    public static void initEmf(String unitName) {
        Log.info("init emf...");
        emf = Persistence.createEntityManagerFactory(unitName);
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration cfg = new Configuration();
                cfg.setProperty(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
                cfg.setProperty(Environment.URL, DatabaseConfig.url);
                cfg.setProperty(Environment.USER, DatabaseConfig.username);
                cfg.setProperty(Environment.PASS, DatabaseConfig.password);
                cfg.setProperty(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
                cfg.setProperty(Environment.SHOW_SQL, "true");
                cfg.setProperty(Environment.HBM2DDL_AUTO, "update");
                cfg.setProperty(Environment.STORAGE_ENGINE, "InnoDB");
                cfg.addAnnotatedClass(User.class);
                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(cfg.getProperties()).build();
                sessionFactory = cfg.buildSessionFactory(serviceRegistry);
            } catch (Throwable ex) {
                Log.error("配置hibernate 失败", ex);
//                throw new ExceptionInInitializerError(ex);
            }
        }
        return sessionFactory;
    }
}
