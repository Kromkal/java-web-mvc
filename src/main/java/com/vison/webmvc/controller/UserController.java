package com.vison.webmvc.controller;

import com.vison.webmvc.entity.User;
import com.vison.webmvc.framework.GetMapping;
import com.vison.webmvc.Response;
import com.vison.webmvc.config.Log;
import com.vison.webmvc.dao.UserMapper;
import com.vison.webmvc.framework.MybatisLoader;
import com.vison.webmvc.framework.PostMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;
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
    public Response user() {
        User user = null;
        SqlSession session = MybatisLoader.getSqlSessionFactory().openSession();
        UserMapper mapper = session.getMapper(UserMapper.class);
        user = mapper.selectUser(1);

        return new Response(0, "获取成功", user);
    }

    @PostMapping(path = "/user/add")
    public Response add(User user) {
        Log.info("request user", user);
        try {
//            EntityManagerFactory emf = Persistence.createEntityManagerFactory("itdoc");
//            Session session = HibernateLoader.sessionFactory.openSession();
//            EntityManager entityManager = emf.createEntityManager();
//            session.save(user);
        } catch (Exception e) {
            Log.error("保存失败", e);
        }

        return new Response(0, "获取成功", user);
    }
}
