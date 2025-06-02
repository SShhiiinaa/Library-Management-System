package com.library.service;

import com.library.dao.ReaderDAO;
import com.library.model.Reader;
import com.library.util.DatabaseUtil;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;
import java.util.regex.Pattern;
import com.library.exception.ServiceException;

public class ReaderService {
    private ReaderDAO readerDAO = new ReaderDAO();

    // 注册业务方法
    public void register(Reader reader) throws ServiceException {
        validateUsername(reader.getUsername());
        validatePassword(reader.getPassword());
        validateContact(reader.getContact());

        try {
            // 检查唯一性约束
            if (readerDAO.isUsernameExists(reader.getUsername())) {
                throw new ServiceException("用户名已被注册");
            }
            if (readerDAO.isContactExists(reader.getContact())) {
                throw new ServiceException("联系方式已被使用");
            }

            // 密码加密
            // 设置默认值
            reader.setMaxBorrow(5);  // 默认借阅上限
            reader.setStatus("A");  // 默认激活状态

            readerDAO.createReader(reader);
        } catch (SQLException e) {
            throw new ServiceException("数据库操作失败: " + e.getMessage());
        }
    }

    // 用户名验证（3-20位字母数字）
    private void validateUsername(String username) throws ServiceException {
        if (!Pattern.matches("^[a-zA-Z0-9]{3,20}$", username)) {
            throw new ServiceException("用户名需3-20位字母或数字");
        }
    }

    // 密码验证（至少8位含字母数字）
    private void validatePassword(String password) throws ServiceException {
        if (!Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", password)) {
            throw new ServiceException("密码需至少8位且包含字母和数字");
        }
    }

    // 联系方式验证（手机号格式）
    private void validateContact(String contact) throws ServiceException {
        if (!Pattern.matches("^1[3-9]\\d{9}$", contact)) {
            throw new ServiceException("请输入有效的手机号码");
        }
    }
}