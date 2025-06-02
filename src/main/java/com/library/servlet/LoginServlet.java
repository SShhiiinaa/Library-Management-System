package com.library.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import com.library.util.DatabaseUtil;
import com.library.model.Reader;
import com.library.model.Admin;
import java.io.IOException;
import java.sql.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String loginType = request.getParameter("loginType");
        boolean isAdmin = "admin".equals(loginType);

        String username;
        String password;
        if (isAdmin) {
            username = request.getParameter("adminUsername");
            password = request.getParameter("adminPassword");
        } else {
            username = request.getParameter("username");
            password = request.getParameter("password");
        }

        // 基础验证
        if (isEmpty(username) || isEmpty(password)) {
            forwardError(request, response, "认证信息不能为空");
            return;
        }

        try (Connection conn = DatabaseUtil.getConnection()) {
            Object user = authenticate(conn, username, password, isAdmin);

            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("role", isAdmin ? "ADMIN" : "USER");
                response.sendRedirect("index.jsp");
            } else {
                forwardError(request, response, "无效的登录凭证");
            }
        } catch (SQLException e) {
            logError(e);
            forwardError(request, response, "数据库异常: " + e.getMessage());
        }
    }

    private Object authenticate(Connection conn, String username, String password, boolean isAdmin)
            throws SQLException {

        String table = isAdmin ? "ADMINISTRATORS" : "READERS";
        String sql = String.format(
                "SELECT * FROM %s WHERE USERNAME = ? AND PASSWORD = ?",
                table
        );

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // 添加管理员权限标识
                if(isAdmin) {
                    Admin admin = new Admin(
                            rs.getInt("ADMINID"),
                            rs.getString("USERNAME")
                    );
                    admin.setRole("ADMIN"); // 需要实体类有setter方法
                    return admin;
                } else {
                    Reader reader = new Reader(
                            rs.getInt("READERID"),
                            rs.getString("USERNAME"),
                            rs.getString("NAME"),
                            rs.getString("STATUS")
                    );
                    reader.setRole("USER");
                    return reader;
                }
            }
            return null;
        }
    }
    private boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private void forwardError(HttpServletRequest request, HttpServletResponse response, String error)
            throws ServletException, IOException {
        request.setAttribute("error", error);
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    private void logError(SQLException e) {
        System.err.println("SQL State: " + e.getSQLState());
        System.err.println("Error Code: " + e.getErrorCode());
        System.err.println("Message: " + e.getMessage());
        e.printStackTrace();
    }
}