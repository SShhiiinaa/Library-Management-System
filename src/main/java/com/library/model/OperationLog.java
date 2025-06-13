package com.library.model;

import java.sql.Timestamp;

public class OperationLog {
    private int id;
    private String operator;         // 操作人用户名
    private String operatorType;     // 操作人类型：ADMIN/READER
    private Timestamp opTime;        // 操作时间
    private String opType;           // 操作类型，如login/register/borrow/return
    private String target;           // 操作目标（如图书ID、用户名等）
    private String ip;               // 操作IP
    private String result;           // 操作结果 success/fail
    private String description;      // 详细描述

    // --- getters and setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }

    public String getOperatorType() { return operatorType; }
    public void setOperatorType(String operatorType) { this.operatorType = operatorType; }

    public Timestamp getOpTime() { return opTime; }
    public void setOpTime(Timestamp opTime) { this.opTime = opTime; }

    public String getOpType() { return opType; }
    public void setOpType(String opType) { this.opType = opType; }

    public String getTarget() { return target; }
    public void setTarget(String target) { this.target = target; }

    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}