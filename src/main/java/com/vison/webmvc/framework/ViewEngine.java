package com.vison.webmvc.framework;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.ServletLoader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import javax.servlet.ServletContext;

/**
 *
 * @author vison.cao <visonforcoding@gmail.com>
 */
public class ViewEngine {

    private static PebbleEngine engine;

    public static void load(ServletContext servletContext) {
        // 定义一个ServletLoader用于加载模板:
        ServletLoader loader = new ServletLoader(servletContext);
        // 模板编码:
        loader.setCharset("UTF-8");
        // 模板前缀，这里默认模板必须放在`/WEB-INF/templates`目录:
        loader.setPrefix("/WEB-INF/templates");
        // 模板后缀:
        loader.setSuffix("");
        // 创建Pebble实例:
        engine = new PebbleEngine.Builder()
                .autoEscaping(true) // 默认打开HTML字符转义，防止XSS攻击
                .cacheActive(false) // 禁用缓存使得每次修改模板可以立刻看到效果
                .loader(loader).build();
    }

    public static String render(String template, Map<String, Object> context) throws IOException {
        Writer writer = new StringWriter();

        PebbleTemplate compiledTemplate = engine.getTemplate(template);
        compiledTemplate.evaluate(writer, context);
        String output = writer.toString();
        return output;

    }

}
