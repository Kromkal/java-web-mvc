package com.vison.webmvc.config;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author vison.cao <visonforcoding@gmail.com>
 */
public class Log {

    private static String msgTraceNo;

    public static void setMsgTraceNo(String msgTraceNo) {
        Log.msgTraceNo = msgTraceNo;
    }

    public static StackTraceElement findCaller() {
        // 获取堆栈信息
        StackTraceElement[] callStack = Thread.currentThread().getStackTrace();
        if (null == callStack) {
            return null;
        }

        // 最原始被调用的堆栈信息
        StackTraceElement caller = null;
        // 日志类名称
        String logClassName = Log.class.getName();
        // 循环遍历到日志类标识
        boolean isEachLogClass = false;

        // 遍历堆栈信息，获取出最原始被调用的方法信息
        for (StackTraceElement s : callStack) {
            // 遍历到日志类
            if (logClassName.equals(s.getClassName())) {
                isEachLogClass = true;
            }
            // 下一个非日志类的堆栈，就是最原始被调用的方法
            if (isEachLogClass) {
                if (!logClassName.equals(s.getClassName())) {
                    isEachLogClass = false;
                    caller = s;
                    break;
                }
            }
        }

        return caller;
    }

    private static Logger logger() {
        // 最原始被调用的堆栈对象
        StackTraceElement caller = findCaller();
        if (null == caller) {
            return LogManager.getLogger(Log.class);
        }
        Logger log = LogManager.getLogger(caller.getClassName() + "." + caller.getMethodName() + "() Line: " + caller.getLineNumber());

        return log;
    }

    public static void debug(String msg, Object o) {
        logger().debug(String.format("%s %s %s", msgTraceNo, msg, o.toString()));
    }

    public static void debug(String msg) {
        logger().debug(String.format("%s %s", msgTraceNo, msg));
    }

    public static void error(String msg, Object o) {
        logger().error(String.format("%s %s %s", msgTraceNo, msg, o.toString()));
    }

    public static void error(String msg) {
        logger().error(String.format("%s %s", msgTraceNo, msg));
    }

    public static void info(String msg, Object o) {
        logger().info(String.format("%s %s %s", msgTraceNo, msg, o.toString()));
    }

    public static void info(String msg) {
        logger().info(String.format("%s %s", msgTraceNo, msg));
    }

}
