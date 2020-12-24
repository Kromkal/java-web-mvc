package com.vison.webmvc.framework;

import com.vison.webmvc.framework.exception.NullRouteException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author vison.cao <visonforcoding@gmail.com>
 */
@WebServlet(name = "DispatchServlet", urlPatterns = {"/"})
public class DispatchServlet extends HttpServlet {

    private static final Logger log = LogManager.getLogger(DispatchServlet.class.getName());

    @Override
    public void init() throws ServletException {
        ViewEngine.load(this.getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        String path = req.getRequestURI().substring(req.getContextPath().length());
        Object res;
        try {
            res = dispatch(path, req, resp);
        } catch (NullRouteException ex) {
            res = "404 Not Found";
        }
        String responseBody = this.handInvokeRes(res);
        resp.getWriter().write(responseBody);
        resp.getWriter().flush();
    }

    private Object dispatch(String path, HttpServletRequest request, HttpServletResponse response)
            throws NullRouteException {
        Method method = this.getMaps(path);
        Object res = null;
        Parameter[] parameters = method.getParameters();
        Object[] arguments = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            log.info(parameter.getType());
            Class<?> parameterClass = parameter.getType();
            String parameterName = parameter.getName();
            if (parameterClass == HttpServletRequest.class) {
                arguments[i] = request;
            } else if (parameterClass == HttpServletResponse.class) {
                arguments[i] = response;
            } else if (parameterClass == HttpSession.class) {
                arguments[i] = request.getSession();
            } else if (parameterClass == int.class) {
                arguments[i] = Integer.valueOf(getOrDefault(request, parameterName, "0"));
            } else if (parameterClass == long.class) {
                arguments[i] = Long.valueOf(getOrDefault(request, parameterName, "0"));
            } else if (parameterClass == boolean.class) {
                arguments[i] = Boolean.valueOf(getOrDefault(request, parameterName, "false"));
            } else if (parameterClass == String.class) {
                arguments[i] = getOrDefault(request, parameterName, "");
            } else {
                throw new RuntimeException("Missing handler for type: " + parameterClass);
            }
        }
        Object obj;
        try {
            obj = method.getDeclaringClass().getDeclaredConstructor().newInstance();
            res = method.invoke(obj, arguments);
        } catch (Exception e) {
            log.error("方法invoke失败", e);
        }
        return res;
    }

    private String getOrDefault(HttpServletRequest request, String name, String defaultValue) {
        String s = request.getParameter(name);
        return s == null ? defaultValue : s;
    }

    private String handInvokeRes(Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        }
        return "";
    }

    private Method getMaps(String path) throws NullRouteException {

        String packageName = "com.vison.webmvc.controller";
        ConfigurationBuilder config = new ConfigurationBuilder();
        config.filterInputsBy(new FilterBuilder().includePackage(packageName));
        config.addUrls(ClasspathHelper.forPackage(packageName));
        config.setScanners(new MethodAnnotationsScanner());
        Reflections f = new Reflections(config);
        Set<Method> resources = f.getMethodsAnnotatedWith(GetMapping.class);

        for (Method method : resources) {
            GetMapping annotation = method.getAnnotation(GetMapping.class);
            if (path.equals(annotation.path())) {
                return method;
            }
        }
        throw new NullRouteException();
    }
}
