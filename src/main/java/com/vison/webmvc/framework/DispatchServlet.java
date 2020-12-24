package com.vison.webmvc.framework;

import com.google.gson.Gson;
import com.vison.webmvc.config.Log;
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

/**
 *
 * @author vison.cao <visonforcoding@gmail.com>
 */
@WebServlet(name = "DispatchServlet", urlPatterns = {"/"})
public class DispatchServlet extends HttpServlet {

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
        Log.info("返回信息", responseBody);
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
            Log.info("参数类型", parameter.getType());
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
            Log.error("方法invoke失败", e);
        }
        return res;
    }

    private String getOrDefault(HttpServletRequest request, String name, String defaultValue) {
        String s = request.getParameter(name);
        return s == null ? defaultValue : s;
    }

    /**
     * 处理actionx
     *
     * @param obj
     * @return
     */
    private String handInvokeRes(Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        }
        Gson gson = new Gson();
        String jsonRes = gson.toJson(obj);
        return jsonRes;
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
