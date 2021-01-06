package com.vison.webmvc.framework;

import com.google.gson.Gson;
import com.vison.webmvc.config.App;
import com.vison.webmvc.config.Log;
import com.vison.webmvc.framework.exception.NullRouteException;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private Reflections f;

    @Override
    public void init() throws ServletException {
        ViewEngine.load(this.getServletContext());
        f = App.f;

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            process(req, resp, RequestMethod.GET);
        } catch (NullRouteException ex) {
            Logger.getLogger(DispatchServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            process(req, resp, RequestMethod.POST);
        } catch (NullRouteException ex) {
            Logger.getLogger(DispatchServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void process(HttpServletRequest req, HttpServletResponse resp, RequestMethod requestMethod)
            throws NullRouteException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        String path = req.getRequestURI().substring(req.getContextPath().length());
        Object res = null;
        try {
            Method invokeMethod = this.getMaps(path, requestMethod);
            switch (requestMethod) {
                case GET:
                    res = getDispatch(req, resp, invokeMethod);
                    break;
                case POST:
                    res = postDispatch(req, resp, invokeMethod);
                    break;
            }
        } catch (NullRouteException ex) {
            res = "404 Not Found";
        } catch (Exception e) {
            Log.error("捕获错误", e);
        }
        String responseBody = this.handInvokeRes(res);
        resp.getWriter().write(responseBody);
        resp.getWriter().flush();
    }

    private Object getDispatch(HttpServletRequest request, HttpServletResponse response, Method invokeMethod) {

        Object res = null;
        Parameter[] parameters = invokeMethod.getParameters();
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
            obj = invokeMethod.getDeclaringClass().getDeclaredConstructor().newInstance();
            res = invokeMethod.invoke(obj, arguments);
        } catch (Exception e) {
            InvocationTargetException targetEx = (InvocationTargetException) e;
            Throwable trowEx = targetEx.getTargetException();
            Log.error("方法invoke失败", trowEx);
        }
        return res;
    }

    private Object postDispatch(HttpServletRequest request, HttpServletResponse response, Method invokeMethod)
            throws IOException {
        Object res = null;
        Parameter[] parameters = invokeMethod.getParameters();
        Object[] arguments = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Log.info("参数类型", parameter.getType());
            Class<?> parameterClass = parameter.getType();
            if (parameterClass == HttpServletRequest.class) {
                arguments[i] = request;
            } else if (parameterClass == HttpServletResponse.class) {
                arguments[i] = response;
            } else if (parameterClass == HttpSession.class) {
                arguments[i] = request.getSession();
            } else {
                // 读取JSON并解析为JavaBean:
                request.setCharacterEncoding("UTF-8");
                BufferedReader reader = request.getReader();
                Gson gson = new Gson();
                arguments[i] = gson.fromJson(reader, parameterClass);
            }
        }
        Object obj;
        try {
            obj = invokeMethod.getDeclaringClass().getDeclaredConstructor().newInstance();
            res = invokeMethod.invoke(obj, arguments);
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

    private Method getMaps(String path, RequestMethod requestMethod) throws NullRouteException {

        if (requestMethod == RequestMethod.GET) {
            Set<Method> resources = f.getMethodsAnnotatedWith(GetMapping.class);
            for (Method method : resources) {
                GetMapping annotation = method.getAnnotation(GetMapping.class);
                if (annotation.path().equals(path)) {
                    return method;
                }
            }
        }
        if (requestMethod == RequestMethod.POST) {
            Set<Method> resources = f.getMethodsAnnotatedWith(PostMapping.class);
            for (Method method : resources) {
                PostMapping annotation = method.getAnnotation(PostMapping.class);
                if (annotation.path().equals(path)) {
                    return method;
                }
            }
        }

        throw new NullRouteException();
    }
}
