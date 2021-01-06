package com.vison.webmvc.framework;

import com.vison.webmvc.config.DatabaseConfig;
import com.vison.webmvc.dao.UserMapper;
import javax.sql.DataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 *
 * @author vison.cao <visonforcoding@gmail.com>
 */
public class MybatisLoader {

    static SqlSessionFactory sqlSessionFactory = null;

    public static SqlSessionFactory getSqlSessionFactory() {
        if (sqlSessionFactory == null) {
            DataSource dataSource = DatabaseConfig.getDataSource();
            TransactionFactory transactionFactory
                    = new JdbcTransactionFactory();
            Environment environment
                    = new Environment("development", transactionFactory, dataSource);
            Configuration configuration = new Configuration(environment);
            configuration.addMapper(UserMapper.class);
            sqlSessionFactory
                    = new SqlSessionFactoryBuilder().build(configuration);
        }
        return sqlSessionFactory;
    }

}
