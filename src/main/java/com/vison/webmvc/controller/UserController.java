package com.vison.webmvc.controller;

import com.vison.webmvc.framework.GetMapping;
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
        return "i am user profile";
    }

}
