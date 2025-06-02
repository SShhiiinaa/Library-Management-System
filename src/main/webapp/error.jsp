<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>错误页面</title>
    <style>
        .error-container {
            max-width: 600px;
            margin: 50px auto;
            padding: 20px;
            border: 1px solid #e0e0e0;
            border-radius: 5px;
            background-color: #f8f9fa;
        }
        .error-title {
            color: #dc3545;
        }
    </style>
</head>
<body>
<div class="error-container">
    <h1 class="error-title">⚠️ 操作失败</h1>
    <c:if test="${not empty error}">
        <p>错误信息：${error}</p>
    </c:if>
    <a href="BookManageServlet">返回图书管理</a>
</div>
</body>
</html>
