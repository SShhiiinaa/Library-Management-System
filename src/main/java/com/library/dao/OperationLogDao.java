package com.library.dao;

import com.library.model.OperationLog;
import com.library.util.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class OperationLogDao {
    public void insert(OperationLog log) {
        String sql = "INSERT INTO OPERATION_LOG " +
                "(ID, OPERATOR, OPERATOR_TYPE, OP_TIME, OP_TYPE, TARGET, IP, RESULT, DESCRIPTION) " +
                "VALUES (OPERATION_LOG_SEQ.NEXTVAL, ?, ?, SYSTIMESTAMP, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, log.getOperator());
            ps.setString(2, log.getOperatorType());
            ps.setString(3, log.getOpType());
            ps.setString(4, log.getTarget());
            ps.setString(5, log.getIp());
            ps.setString(6, log.getResult());
            ps.setString(7, log.getDescription());
            ps.executeUpdate();
        } catch (Exception e) {
            // 可考虑写入本地文件或报警
            e.printStackTrace();
        }
    }
}