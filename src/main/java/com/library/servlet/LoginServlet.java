package com.library.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import com.library.util.DatabaseUtil;
import com.library.dao.UserRoleDao;
import com.library.model.Reader;
import com.library.model.Admin;
import com.library.util.InputSanitizer;
import com.library.util.OperationLogger; // 引入日志工具
import java.io.IOException;
import java.sql.*;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserRoleDao userRoleDao = new UserRoleDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String loginType = InputSanitizer.sanitizeIdentifier(request.getParameter("loginType"), 10);
        boolean isAdmin = "admin".equals(loginType);

        String username;
        String password;
        if (isAdmin) {
            username = InputSanitizer.sanitizeIdentifier(request.getParameter("adminUsername"), 20);
            password = request.getParameter("adminPassword");
        } else {
            username = InputSanitizer.sanitizeIdentifier(request.getParameter("username"), 20);
            password = request.getParameter("password");
        }

        if (isEmpty(username) || isEmpty(password)) {
            OperationLogger.log(request, "login", username, "fail", "认证信息为空");
            forwardError(request, response, "认证信息不能为空");
            return;
        }

        if (!username.matches("^[a-zA-Z0-9_@.]{3,20}$")) {
            OperationLogger.log(request, "login", username, "fail", "用户名格式不合法");
            forwardError(request, response, "用户名格式不合法");
            return;
        }
        if (password.length() > 64) {
            OperationLogger.log(request, "login", username, "fail", "密码长度过长");
            forwardError(request, response, "密码长度过长");
            return;
        }

        try (Connection conn = DatabaseUtil.getConnection()) {
            Object user = findUserByUsername(conn, username, isAdmin);

            if (user != null) {
                String passwordHash;
                if (isAdmin) {
                    Admin admin = (Admin) user;
                    passwordHash = admin.getPassword();
                } else {
                    Reader reader = (Reader) user;
                    passwordHash = reader.getPassword();
                }
                boolean valid;
                try {
                    valid = BCrypt.checkpw(password, passwordHash);
                } catch (IllegalArgumentException ex) {
                    valid = false;
                }
                if (!valid) {
                    OperationLogger.log(request, "login", username, "fail", "用户名或密码错误");
                    forwardError(request, response, "用户名或密码错误");
                    return;
                }

                // 查询并注入角色
                int userId;
                String userType;
                List<String> roles;
                if (isAdmin) {
                    Admin admin = (Admin) user;
                    userId = admin.getUserId();
                    userType = "A";
                    roles = userRoleDao.getRoleNamesByUserId(userId, userType);
                    if (roles == null || roles.isEmpty()) {
                        roles = java.util.Arrays.asList("ADMIN");
                    } else {
                        roles = roles.stream()
                                .map(s -> {
                                    if ("reader".equalsIgnoreCase(s)) return "USER";
                                    if ("admin".equalsIgnoreCase(s)) return "ADMIN";
                                    return s.toUpperCase();
                                })
                                .collect(java.util.stream.Collectors.toList());
                    }
                    admin.setRoles(roles);
                } else {
                    Reader reader = (Reader) user;
                    userId = reader.getUserId();
                    userType = "R";
                    roles = userRoleDao.getRoleNamesByUserId(userId, userType);
                    if (roles == null || roles.isEmpty()) {
                        roles = java.util.Arrays.asList("USER");
                    } else {
                        roles = roles.stream()
                                .map(s -> {
                                    if ("reader".equalsIgnoreCase(s)) return "USER";
                                    if ("admin".equalsIgnoreCase(s)) return "ADMIN";
                                    return s.toUpperCase();
                                })
                                .collect(java.util.stream.Collectors.toList());
                    }
                    reader.setRoles(roles);
                }

                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                OperationLogger.log(request, "login", username, "success", "用户登录成功");
                response.sendRedirect("index.jsp");
            } else {
                OperationLogger.log(request, "login", username, "fail", "无效的登录凭证");
                forwardError(request, response, "无效的登录凭证");
            }
        } catch (SQLException e) {
            logError(e);
            OperationLogger.log(request, "login", username, "fail", "数据库异常: " + e.getMessage());
            forwardError(request, response, "数据库异常: " + e.getMessage());
        }
    }

    private Object findUserByUsername(Connection conn, String username, boolean isAdmin)
            throws SQLException {

        String table = isAdmin ? "ADMINISTRATORS" : "READERS";
        String sql = String.format(
                "SELECT * FROM %s WHERE USERNAME = ?",
                table
        );

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                if(isAdmin) {
                    Admin admin = new Admin();
                    admin.setAdminId(rs.getInt("ADMINID"));
                    admin.setUsername(rs.getString("USERNAME"));
                    admin.setPassword(rs.getString("PASSWORD"));
                    if (hasColumn(rs, "NAME")) admin.setName(rs.getString("NAME"));
                    return admin;
                } else {
                    Reader reader = new Reader();
                    reader.setReaderId(rs.getInt("READERID"));
                    reader.setUsername(rs.getString("USERNAME"));
                    reader.setPassword(rs.getString("PASSWORD"));
                    reader.setName(rs.getString("NAME"));
                    reader.setStatus(rs.getString("STATUS"));
                    return reader;
                }
            }
            return null;
        }
    }

    private boolean hasColumn(ResultSet rs, String columnName) {
        try {
            rs.findColumn(columnName);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private void forwardError(HttpServletRequest request, HttpServletResponse response, String error)
            throws ServletException, IOException {
        request.setAttribute("error", InputSanitizer.sanitizeForHtml(error));
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    private void logError(SQLException e) {
        System.err.println("SQL State: " + e.getSQLState());
        System.err.println("Error Code: " + e.getErrorCode());
        System.err.println("Message: " + e.getMessage());
        e.printStackTrace();
    }
}