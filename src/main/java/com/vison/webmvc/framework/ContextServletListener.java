package com.vison.webmvc.framework;

import com.vison.webmvc.config.App;
import com.vison.webmvc.config.Log;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

/**
 * Web application lifecycle listener.
 *
 * @author vison.cao <visonforcoding@gmail.com>
 */
@WebListener
public class ContextServletListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Log.info("应用程序启动...");
        ViewEngine.load(sce.getServletContext());
        String packageName = "com.vison.webmvc.controller";
        ConfigurationBuilder config = new ConfigurationBuilder();
        config.filterInputsBy(new FilterBuilder().includePackage(packageName));
        config.addUrls(ClasspathHelper.forPackage(packageName));
        config.setScanners(new MethodAnnotationsScanner());
        App.f = new Reflections(config);
//        MybatisLoader.getSqlSessionFactory();
//        HibernateLoader.initEmf("itdoc");
        HibernateLoader.getSessionFactory();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
