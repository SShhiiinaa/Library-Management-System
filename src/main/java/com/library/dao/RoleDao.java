package com.library.dao;

import com.library.model.Role;
import com.library.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDao {
    // 添加角色
    public void createRole(Role role) throws SQLException {
        String sql = "INSERT INTO roles (rolename) VALUES (?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, role.getRoleName());
            pstmt.executeUpdate();
        }
    }

    // 查询所有角色
    public List<Role> getAllRoles() throws SQLException {
        List<Role> list = new ArrayList<>();
        String sql = "SELECT * FROM roles ORDER BY roleid";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Role r = new Role();
                r.setRoleId(rs.getInt("roleid"));
                r.setRoleName(rs.getString("rolename"));
                list.add(r);
            }
        }
        return list;
    }

    // 根据ID查找角色
    public Role getRoleById(int roleId) throws SQLException {
        String sql = "SELECT * FROM roles WHERE roleid=?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, roleId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Role r = new Role();
                    r.setRoleId(rs.getInt("roleid"));
                    r.setRoleName(rs.getString("rolename"));
                    return r;
                }
            }
        }
        return null;
    }

    // 根据名称查找角色
    public Role getRoleByName(String roleName) throws SQLException {
        String sql = "SELECT * FROM roles WHERE rolename = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, roleName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Role r = new Role();
                    r.setRoleId(rs.getInt("roleid"));
                    r.setRoleName(rs.getString("rolename"));
                    return r;
                }
            }
        }
        return null;
    }
}