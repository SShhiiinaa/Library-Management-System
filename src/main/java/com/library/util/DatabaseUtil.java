package com.library.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseUtil {
    private static HikariDataSource dataSource;

    static {
        try {
            // 显式加载Oracle驱动
            Class.forName("oracle.jdbc.OracleDriver");

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:oracle:thin:@localhost:1521:orcl");
            config.setUsername("scott");
            config.setPassword("123456");
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(5);

            dataSource = new HikariDataSource(config);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load Oracle JDBC driver", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}