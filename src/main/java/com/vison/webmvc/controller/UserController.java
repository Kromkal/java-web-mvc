package com.vison.webmvc.controller;

import com.vison.webmvc.entity.User;
import com.vison.webmvc.framework.GetMapping;
import com.vison.webmvc.Response;
import com.vison.webmvc.config.Log;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        User user = new User();
        user.setEmail("visonforcoding@gmail.com");
        user.setName("曹麦穗");
        Log.debug("request user", user);
        return new Response(0, "获取成功", user);
    }

}
