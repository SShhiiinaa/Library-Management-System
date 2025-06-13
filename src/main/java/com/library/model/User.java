package com.library.model;

import java.util.List;

public interface User {
    Integer getUserId();
    String getUsername();
    String getName();
    String getPassword();
    String getUserType();            // "A"=管理员, "R"=读者
    List<String> getRoles();
    void setRoles(List<String> roles);
}