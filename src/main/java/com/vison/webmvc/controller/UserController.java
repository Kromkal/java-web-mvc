package com.vison.webmvc.controller;

import com.vison.webmvc.entity.User;
import com.vison.webmvc.framework.GetMapping;
import com.vison.webmvc.Response;
import com.vison.webmvc.config.Log;
import com.vison.webmvc.dao.UserMapper;
import com.vison.webmvc.framework.HibernateLoader;
import com.vison.webmvc.framework.MybatisLoader;
import com.vison.webmvc.framework.PostMapping;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;
import org.hibernate.Session;
import org.hibernate.Transaction;
//import org.hibernate.Session;
//import org.hibernate.Transaction;

/**
 *
 * @author vison.cao <visonforcoding@gmail.com>
 */
public class UserController {

    public UserController() {
    }

    @GetMapping(path = "/user/profile")
    public String profile(HttpServletRequest request, HttpServletResponse response) {
        System.out.print(request.getCookies());
        return "i am user profile";
    }

    @GetMapping(path = "/user")
    public Response user(HttpServletRequest request, int id) {
        User user = null;
        SqlSession session = MybatisLoader.getSqlSessionFactory().openSession();
        UserMapper mapper = session.getMapper(UserMapper.class);
        user = mapper.selectUser(id);
        return new Response(0, "获取成功", user);
    }

    @PostMapping(path = "/user/add")
    public Response add(User user) {
        Log.info("request user", user);
        Transaction transaction = null;
        Session session = HibernateLoader.getSessionFactory().openSession(); // start a transaction
        transaction = session.beginTransaction();
        // save the student object
        session.save(user);
        // commit transaction
        transaction.commit();
        session.close();
        return new Response(0, "保存");
    }

    @GetMapping(path = "/user/delete")
    public Response delete(int id) {
        Log.info("id", id);
        EntityManagerFactory emf = HibernateLoader.emf;
        EntityManager entityManager = emf.createEntityManager();
        User user = entityManager.find(User.class, id);
        Log.info("user", user);
        entityManager.remove(user);
        return new Response(0, "删除成功");
    }
}
