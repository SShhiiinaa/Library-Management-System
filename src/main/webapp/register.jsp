<%@ page contentType="text/html;charset=GBK" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>用户注册</title>
    <meta charset="GBK">
    <style>
        /* 保持原有样式不变 */
        :root { --primary-color: #2c3e50; --error-color: #e74c3c; }
        body { font-family: 'Segoe UI', sans-serif; display: flex; justify-content: center; align-items: center; min-height: 100vh; background: #f5f6fa; margin: 0; }
        .container { background: white; padding: 2rem; border-radius: 8px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); width: 100%; max-width: 400px; }
        .error-box { background: #fee; border: 1px solid var(--error-color); color: var(--error-color); padding: 1rem; border-radius: 4px; margin-bottom: 1.5rem; }
        .form-group { margin-bottom: 1.5rem; }
        label { display: block; margin-bottom: 0.5rem; color: var(--primary-color); font-weight: 500; }
        input[type="text"], input[type="password"], select {
            width: 100%; padding: 0.8rem; border: 1px solid #ddd; border-radius: 4px; font-size: 1rem; /* 统一输入框样式 */
        }
        button[type="submit"] { width: 100%; padding: 1rem; background: var(--primary-color); color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 1rem; }
        .login-link { text-align: center; margin-top: 1.5rem; }
    </style>
</head>
<body>
<div class="container">
    <h2>用户注册</h2>

    <c:if test="${not empty requestScope.error}">
        <div class="error-box"><c:out value="${requestScope.error}"/></div>
    </c:if>

    <form action="${pageContext.request.contextPath}/register" method="post">
        <!-- 新增字段 -->
        <div class="form-group">
            <label>真实姓名:</label>
            <input type="text" name="name" required
                   placeholder="请输入姓名">
        </div>

        <div class="form-group">
            <label>性别:</label>
            <select name="gender" required>
                <option value="">请选择性别</option>
                <option value="M">男</option>
                <option value="F">女</option>
            </select>
        </div>

        <div class="form-group">
            <label>手机号码:</label>
            <input type="text" name="contact" required
                   pattern="^1[3-9]\d{9}$"
                   placeholder="请输入11位手机号码"
                   title="请输入有效的手机号码">
        </div>

        <!-- 原有字段 -->
        <div class="form-group">
            <label>用户名:</label>
            <input type="text" name="username" required
                   placeholder="3-20位字母或数字" pattern="[A-Za-z0-9]{3,20}">
        </div>

        <div class="form-group">
            <label>密码:</label>
            <input type="password" name="password" required
                   placeholder="至少8位含字母和数字"
                   pattern="^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$">
        </div>

        <div class="form-group">
            <label>确认密码:</label>
            <input type="password" name="confirmPassword" required
                   placeholder="请再次输入密码">
        </div>

        <button type="submit">立即注册</button>
    </form>

    <div class="login-link">
        已有账户？<a href="${pageContext.request.contextPath}/login.jsp">去登录</a>
    </div>
</div>

<!-- 前端增强验证 -->
<script>
    document.querySelector('form').addEventListener('submit', function(e) {
        // 密码一致性验证
        const pwd = document.querySelector('[name="password"]').value;
        const confirmPwd = document.querySelector('[name="confirmPassword"]').value;
        if (pwd !== confirmPwd) {
            alert('两次输入的密码不一致');
            e.preventDefault();
        }
    });
</script>
</body>
</html>