package com.library.servlet;

import com.library.model.Reader;
import com.library.service.ReaderService;
import com.library.exception.ServiceException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.regex.Pattern;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final ReaderService readerService = new ReaderService();

    // 正则验证模式（移除了中文姓名验证）
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // 获取所有请求参数
        String username = getParameterSafely(request, "username");
        String password = getParameterSafely(request, "password");
        String confirm = getParameterSafely(request, "confirmPassword");
        String name = getParameterSafely(request, "name");
        String contact = getParameterSafely(request, "contact");
        String gender = getParameterSafely(request, "gender");

        try {
            // 执行多维度验证
            validatePasswordMatch(password, confirm);
            validateNameNotEmpty(name);          // 修改后的姓名验证
            validateMobileNumber(contact);
            validateGenderSelection(gender);

            // 构建Reader对象
            Reader reader = new Reader();
            reader.setUsername(username);
            reader.setPassword(password); // 实际加密在Service层完成
            reader.setName(name.trim());
            reader.setContact(contact);
            reader.setGender(parseGender(gender));

            // 执行业务注册
            readerService.register(reader);

            // 注册成功跳转
            response.sendRedirect("login.jsp?regSuccess=true");
        } catch (ServiceException e) {
            handleRegistrationError(request, response, e.getMessage());
        }
    }

    private String getParameterSafely(HttpServletRequest request, String paramName) {
        String value = request.getParameter(paramName);
        return value != null ? value.trim() : "";
    }

    private void validatePasswordMatch(String pwd, String confirmPwd) throws ServiceException {
        if (!pwd.equals(confirmPwd)) {
            throw new ServiceException("两次输入的密码不一致");
        }
    }

    // 修改后的姓名验证方法
    private void validateNameNotEmpty(String name) throws ServiceException {
        if (name == null || name.trim().isEmpty()) {
            throw new ServiceException("姓名不能为空");
        }
    }

    private void validateMobileNumber(String contact) throws ServiceException {
        if (!MOBILE_PATTERN.matcher(contact).matches()) {
            throw new ServiceException("请输入有效的11位手机号码");
        }
    }

    private void validateGenderSelection(String gender) throws ServiceException {
        if (gender.length() != 1 || !"MF".contains(gender)) {
            throw new ServiceException("请选择有效性别");
        }
    }
    private String parseGender(String gender) {
        // 只取第一个字符，且大写，防止有“男”“女”等传入
        if (gender == null || gender.trim().isEmpty()) return null;
        String g = gender.trim().toUpperCase();
        if (g.startsWith("M")) return "M";
        if (g.startsWith("F")) return "F";
        return g.substring(0, 1); // 其他情况只取第一个字符
    }
    private void handleRegistrationError(HttpServletRequest request,
                                         HttpServletResponse response,
                                         String errorMsg)
            throws ServletException, IOException {
        request.setAttribute("error", errorMsg);
        request.getRequestDispatcher("/register.jsp")
                .forward(request, response);
    }
}