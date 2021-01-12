package com.vison.webmvc.controller;

import com.vison.webmvc.framework.GetMapping;
import com.vison.webmvc.framework.ViewEngine;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author vison.cao <visonforcoding@gmail.com>
 */
public class HelloController {

    @GetMapping(path = "/hello")
    public String hello() throws IOException {
        Map<String, Object> context = new HashMap<>();
        context.put("vison", "visonforcoding");
        return ViewEngine.render("/hello.pebble", context);
    }

}
