<%@ page contentType="text/html;charset=GBK" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>�û�ע��</title>
    <meta charset="GBK">
    <style>
        /* ����ԭ����ʽ���� */
        :root { --primary-color: #2c3e50; --error-color: #e74c3c; }
        body { font-family: 'Segoe UI', sans-serif; display: flex; justify-content: center; align-items: center; min-height: 100vh; background: #f5f6fa; margin: 0; }
        .container { background: white; padding: 2rem; border-radius: 8px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); width: 100%; max-width: 400px; }
        .error-box { background: #fee; border: 1px solid var(--error-color); color: var(--error-color); padding: 1rem; border-radius: 4px; margin-bottom: 1.5rem; }
        .form-group { margin-bottom: 1.5rem; }
        label { display: block; margin-bottom: 0.5rem; color: var(--primary-color); font-weight: 500; }
        input[type="text"], input[type="password"], select {
            width: 100%; padding: 0.8rem; border: 1px solid #ddd; border-radius: 4px; font-size: 1rem; /* ͳһ�������ʽ */
        }
        button[type="submit"] { width: 100%; padding: 1rem; background: var(--primary-color); color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 1rem; }
        .login-link { text-align: center; margin-top: 1.5rem; }
    </style>
</head>
<body>
<div class="container">
    <h2>�û�ע��</h2>

    <c:if test="${not empty requestScope.error}">
        <div class="error-box"><c:out value="${requestScope.error}"/></div>
    </c:if>

    <form action="${pageContext.request.contextPath}/register" method="post">
        <!-- �����ֶ� -->
        <div class="form-group">
            <label>��ʵ����:</label>
            <input type="text" name="name" required
                   placeholder="����������">
        </div>

        <div class="form-group">
            <label>�Ա�:</label>
            <select name="gender" required>
                <option value="">��ѡ���Ա�</option>
                <option value="M">��</option>
                <option value="F">Ů</option>
            </select>
        </div>

        <div class="form-group">
            <label>�ֻ�����:</label>
            <input type="text" name="contact" required
                   pattern="^1[3-9]\d{9}$"
                   placeholder="������11λ�ֻ�����"
                   title="��������Ч���ֻ�����">
        </div>

        <!-- ԭ���ֶ� -->
        <div class="form-group">
            <label>�û���:</label>
            <input type="text" name="username" required
                   placeholder="3-20λ��ĸ������" pattern="[A-Za-z0-9]{3,20}">
        </div>

        <div class="form-group">
            <label>����:</label>
            <input type="password" name="password" required
                   placeholder="����8λ����ĸ������"
                   pattern="^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$">
        </div>

        <div class="form-group">
            <label>ȷ������:</label>
            <input type="password" name="confirmPassword" required
                   placeholder="���ٴ���������">
        </div>

        <button type="submit">����ע��</button>
    </form>

    <div class="login-link">
        �����˻���<a href="${pageContext.request.contextPath}/login.jsp">ȥ��¼</a>
    </div>
</div>

<!-- ǰ����ǿ��֤ -->
<script>
    document.querySelector('form').addEventListener('submit', function(e) {
        // ����һ������֤
        const pwd = document.querySelector('[name="password"]').value;
        const confirmPwd = document.querySelector('[name="confirmPassword"]').value;
        if (pwd !== confirmPwd) {
            alert('������������벻һ��');
            e.preventDefault();
        }
    });
</script>
</body>
</html>