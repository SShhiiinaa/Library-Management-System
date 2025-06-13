package com.library.servlet;

import com.library.util.InputSanitizer;
import com.library.util.OperationLogger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        String username = null;
        if (session != null) {
            Object userObj = session.getAttribute("user");
            if (userObj != null) {
                // 尝试获取用户名进行日志记录
                // 假设user对象有getUsername()方法
                try {
                    username = (String) userObj.getClass().getMethod("getUsername").invoke(userObj);
                    username = InputSanitizer.sanitizeIdentifier(username, 20);
                } catch (Exception e) {
                    username = "unknown";
                }
            }
            session.invalidate();
            // 日志：登出成功
            OperationLogger.log(req, "logout", username, "success", "用户安全退出登录");
        } else {
            // 日志：未登录状态下访问logout
            OperationLogger.log(req, "logout", null, "success", "未登录状态下访问logout");
        }

        resp.sendRedirect(req.getContextPath() + "/login.jsp");
    }
}