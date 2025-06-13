package com.library.servlet;

import com.library.model.Reader;
import com.library.service.ReaderService;
import com.library.exception.ServiceException;
import com.library.util.InputSanitizer;
import com.library.util.OperationLogger; // 日志工具
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.regex.Pattern;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final ReaderService readerService = new ReaderService();

    // 手机号正则
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    // 用户名正则（字母数字下划线，3-20位）
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,20}$");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // 用InputSanitizer过滤所有输入
        String username = InputSanitizer.sanitizeIdentifier(request.getParameter("username"), 20);
        String password = request.getParameter("password"); // 密码可不做前端过滤
        String confirm = request.getParameter("confirmPassword");
        String name = InputSanitizer.sanitizeText(request.getParameter("name"), 20);
        String contact = InputSanitizer.sanitizeMobile(request.getParameter("contact"));
        String gender = InputSanitizer.sanitizeIdentifier(request.getParameter("gender"), 1);

        try {
            validateUsername(username);
            validatePasswordMatch(password, confirm);
            validatePasswordStrength(password);
            validateNameNotEmpty(name);
            validateMobileNumber(contact);
            validateGenderSelection(gender);

            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            Reader reader = new Reader();
            reader.setUsername(username);
            reader.setPassword(hashedPassword);
            reader.setName(name);
            reader.setContact(contact);
            reader.setGender(parseGender(gender));

            readerService.register(reader);

            // 注册成功日志
            OperationLogger.log(request, "register", username, "success", "用户注册成功");

            response.sendRedirect("login.jsp?regSuccess=true");
        } catch (ServiceException e) {
            // 注册失败日志
            OperationLogger.log(request, "register", username, "fail", "注册失败：" + e.getMessage());
            handleRegistrationError(request, response, e.getMessage());
        }
    }

    private void validateUsername(String username) throws ServiceException {
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            throw new ServiceException("用户名应为3-20位字母、数字或下划线");
        }
    }

    private void validatePasswordMatch(String pwd, String confirmPwd) throws ServiceException {
        if (pwd == null || confirmPwd == null || !pwd.equals(confirmPwd)) {
            throw new ServiceException("两次输入的密码不一致");
        }
    }

    private void validatePasswordStrength(String password) throws ServiceException {
        if (password == null || !password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")) {
            throw new ServiceException("密码需至少8位且包含字母和数字");
        }
    }

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
        if (gender == null || gender.length() != 1 || !"MF".contains(gender)) {
            throw new ServiceException("请选择有效性别");
        }
    }

    private String parseGender(String gender) {
        if (gender == null || gender.trim().isEmpty()) return null;
        String g = gender.trim().toUpperCase();
        if (g.startsWith("M")) return "M";
        if (g.startsWith("F")) return "F";
        return g.substring(0, 1);
    }

    private void handleRegistrationError(HttpServletRequest request,
                                         HttpServletResponse response,
                                         String errorMsg)
            throws ServletException, IOException {
        request.setAttribute("error", InputSanitizer.sanitizeForHtml(errorMsg));
        request.getRequestDispatcher("/register.jsp")
                .forward(request, response);
    }
}