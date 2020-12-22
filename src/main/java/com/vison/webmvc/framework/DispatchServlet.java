package com.vison.webmvc.framework;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author vison.cao <visonforcoding@gmail.com>
 */
@WebServlet(name = "DispatchServlet", urlPatterns = {"/"})
public class DispatchServlet extends HttpServlet {

    private Map<String, GetDispatcher> getMappings = new HashMap<>();
    private Map<String, PostDispatcher> postMappings = new HashMap<>();
    private ViewEngine viewEngine;

    @Override
    public void init() throws ServletException {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        String path = req.getRequestURI().substring(req.getContextPath().length());
        log(path);
        resp.getWriter().write(path);
        resp.getWriter().flush();
        // 根据路径查找GetDispatcher:
        // 调用Controller方法获得返回值:
    }
}
