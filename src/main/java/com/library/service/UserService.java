//package com.library.service;
//import com.library.model.User;         // User实体类
//import com.library.util.DatabaseUtil; // 数据库工具类
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import com.library.exception.AuthenticationException;// SQL异常
//public class UserService {
//    public User authenticate(String username, String password) throws SQLException, AuthenticationException {
//        String sql = "SELECT user_id, username, role FROM users " +
//                "WHERE username = ? AND password = ?";
//
//        try (Connection conn = DatabaseUtil.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//            stmt.setString(1, username);
//            stmt.setString(2, password);
//
//            ResultSet rs = stmt.executeQuery();
//            if (rs.next()) {
//                User user = new User();
//                user.setUserId(rs.getInt("user_id"));
//                user.setUsername(rs.getString("username"));
//                user.setRole(rs.getString("role"));
//                return user;
//            } else {
//                throw new AuthenticationException("认证失败"); //
//            }
//        }
//    }
//}