package com.library.util;

import com.library.model.OperationLog;
import com.library.dao.OperationLogDao;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;

/**
 * 业务层/Servlet中调用此类快速记录操作日志
 */
public class OperationLogger {
    private static final OperationLogDao dao = new OperationLogDao();

    /**
     * 记录日志（常用）
     * @param request   HttpServletRequest对象
     * @param opType    操作类型，如login/register/borrow
     * @param target    操作目标，如用户名、图书ID等
     * @param result    操作结果 success/fail
     * @param description  详细描述
     */
    public static void log(HttpServletRequest request, String opType, String target, String result, String description) {
        String operator = "anonymous";
        String operatorType = "GUEST";
        if (request != null) {
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("user") != null) {
                Object user = session.getAttribute("user");
                operator = getUsernameFromUser(user);
                operatorType = getUserTypeFromUser(user);
            }
        }
        String ip = request != null ? request.getRemoteAddr() : "unknown";
        OperationLog log = new OperationLog();
        log.setOperator(operator);
        log.setOperatorType(operatorType);
        log.setOpTime(new Timestamp(System.currentTimeMillis()));
        log.setOpType(opType);
        log.setTarget(target);
        log.setIp(ip);
        log.setResult(result);
        log.setDescription(description);
        dao.insert(log);
    }

    // 可重载方法，便于非Servlet层也能用
    public static void log(String operator, String operatorType, String opType, String target, String ip, String result, String description) {
        OperationLog log = new OperationLog();
        log.setOperator(operator);
        log.setOperatorType(operatorType);
        log.setOpTime(new Timestamp(System.currentTimeMillis()));
        log.setOpType(opType);
        log.setTarget(target);
        log.setIp(ip);
        log.setResult(result);
        log.setDescription(description);
        dao.insert(log);
    }

    private static String getUsernameFromUser(Object user) {
        if (user == null) return "anonymous";
        try {
            // 假设Admin/Reader都有getUsername方法
            return (String)user.getClass().getMethod("getUsername").invoke(user);
        } catch (Exception e) {
            return user.toString();
        }
    }

    private static String getUserTypeFromUser(Object user) {
        if (user == null) return "GUEST";
        String className = user.getClass().getSimpleName().toUpperCase();
        if (className.contains("ADMIN")) return "ADMIN";
        if (className.contains("READER")) return "READER";
        return className;
    }
}