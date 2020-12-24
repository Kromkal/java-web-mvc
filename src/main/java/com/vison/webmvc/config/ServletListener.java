package com.vison.webmvc.config;

import java.util.UUID;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;

/**
 * Web application lifecycle listener.
 *
 * @author vison.cao <visonforcoding@gmail.com>
 */
@WebListener
public class ServletListener implements ServletRequestListener {
    
    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
    }
    
    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        App._uniq_req_no = UUID.randomUUID().toString();
        Log.setMsgTraceNo(App._uniq_req_no);
    }
}
