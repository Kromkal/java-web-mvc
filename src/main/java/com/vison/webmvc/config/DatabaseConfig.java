package com.vison.webmvc.config;

import java.util.Properties;
import javax.sql.DataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;

/**
 *
 * @author vison.cao <visonforcoding@gmail.com>
 */
public class DatabaseConfig {

    static String driver = "com.mysql.jdbc.Driver";
    static String url = "jdbc:mysql://localhost:3306/db_itdoc?useSSL=false";
    static String username = "root";
    static String password = "12345678";

    public static DataSource getDataSource() {
        Properties properties = new Properties();
        properties.setProperty("driver", driver);
        properties.setProperty("url", url);
        properties.setProperty("username", username);
        properties.setProperty("password", password);
        UnpooledDataSourceFactory unpooledDataSourceFactory = new UnpooledDataSourceFactory();
        unpooledDataSourceFactory.setProperties(properties);
        DataSource dataSource = unpooledDataSourceFactory.getDataSource();
        return dataSource;
    }

}
