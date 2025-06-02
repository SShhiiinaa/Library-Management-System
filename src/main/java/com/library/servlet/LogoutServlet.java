package com.library.servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 1. 销毁会话
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // 2. 带上下文路径的重定向
        resp.sendRedirect(req.getContextPath() + "/login.jsp");
    }
}