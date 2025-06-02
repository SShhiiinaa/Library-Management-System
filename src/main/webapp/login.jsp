<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    // 已登录用户直接跳转首页
    if (session.getAttribute("user") != null) {
        response.sendRedirect("index.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>图书管理系统 - 登录</title>
    <style>
        :root {
            --primary-color: #2c3e50;
            --error-color: #e74c3c;
            --tab-bg: #ecf0f1;
            --tab-active-bg: #ffffff;
            --tab-active-color: var(--primary-color);
            --tab-inactive-color: #888;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
            margin: 0;
        }

        .login-container {
            background: white;
            padding: 2.5rem;
            border-radius: 12px;
            box-shadow: 0 8px 30px rgba(0,0,0,0.1);
            width: 100%;
            max-width: 400px;
            transition: transform 0.3s ease;
        }

        .login-container:hover {
            transform: translateY(-5px);
        }

        .logo {
            text-align: center;
            margin-bottom: 1.2rem;
        }

        .logo h1 {
            color: var(--primary-color);
            margin: 0;
            font-size: 2.2em;
        }

        /* 登录方式切换tab */
        .login-tabs {
            display: flex;
            justify-content: center;
            margin-bottom: 2rem;
        }
        .login-tab {
            flex: 1;
            text-align: center;
            padding: 0.8rem 0;
            background: var(--tab-bg);
            color: var(--tab-inactive-color);
            cursor: pointer;
            border: none;
            outline: none;
            font-size: 1.08em;
            border-radius: 7px 7px 0 0;
            margin: 0 2px;
            transition: background 0.2s, color 0.2s;
        }
        .login-tab.active {
            background: var(--tab-active-bg);
            color: var(--tab-active-color);
            font-weight: bold;
            border-bottom: 2px solid var(--primary-color);
        }

        .alert {
            padding: 0.8rem;
            border-radius: 6px;
            margin-bottom: 1.5rem;
        }

        .alert-error {
            background-color: #f8d7da;
            border: 1px solid #f5c6cb;
            color: #721c24;
        }

        .form-group {
            margin-bottom: 1.5rem;
        }

        .form-group label {
            display: block;
            margin-bottom: 0.5rem;
            color: var(--primary-color);
            font-weight: 500;
        }

        .form-control {
            width: 100%;
            padding: 0.8rem;
            border: 1px solid #ddd;
            border-radius: 6px;
            font-size: 1rem;
            transition: border-color 0.3s ease;
        }

        .form-control:focus {
            outline: none;
            border-color: var(--primary-color);
            box-shadow: 0 0 0 3px rgba(44, 62, 80, 0.1);
        }

        .btn-login {
            width: 100%;
            padding: 1rem;
            background-color: var(--primary-color);
            color: white;
            border: none;
            border-radius: 6px;
            font-size: 1rem;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        .btn-login:hover {
            background-color: #34495e;
        }

        .footer-text {
            text-align: center;
            margin-top: 1.5rem;
            color: #666;
        }

        .register-link {
            text-align: center;
            margin-top: 0.8rem;
            color: #666;
            font-size: 0.98em;
        }

        @media (max-width: 480px) {
            .login-container {
                margin: 1rem;
                padding: 1.5rem;
            }
        }
    </style>
    <script>
        // JS切换登录类型
        function showTab(tab) {
            var userForm = document.getElementById('user-login-form');
            var adminForm = document.getElementById('admin-login-form');
            var userTab = document.getElementById('tab-user');
            var adminTab = document.getElementById('tab-admin');
            if(tab === 'user') {
                userForm.style.display = '';
                adminForm.style.display = 'none';
                userTab.classList.add('active');
                adminTab.classList.remove('active');
            } else {
                userForm.style.display = 'none';
                adminForm.style.display = '';
                userTab.classList.remove('active');
                adminTab.classList.add('active');
            }
        }
        window.onload = function() {
            // 默认激活普通用户登录
            showTab('user');
        };
    </script>
</head>
<body>
<div class="login-container">
    <div class="logo">
        <h1>📚 图书管理系统</h1>
    </div>

    <!-- 登录方式tab切换 -->
    <div class="login-tabs">
        <button type="button" class="login-tab active" id="tab-user" onclick="showTab('user')">普通用户登录</button>
        <button type="button" class="login-tab" id="tab-admin" onclick="showTab('admin')">管理员登录</button>
    </div>

    <!-- 错误提示（共用） -->
    <c:if test="${not empty error}">
        <div class="alert alert-error">
                ${error}
        </div>
    </c:if>

    <!-- 普通用户登录表单 -->
    <form id="user-login-form" action="login" method="post" style="display:none;">
        <div class="form-group">
            <label for="username">用户名</label>
            <input
                    type="text"
                    id="username"
                    name="username"
                    class="form-control"
                    required
                    autofocus
                    placeholder="请输入用户名"
            >
        </div>

        <div class="form-group">
            <label for="password">密码</label>
            <input
                    type="password"
                    id="password"
                    name="password"
                    class="form-control"
                    required
                    placeholder="请输入密码"
            >
        </div>

        <button type="submit" class="btn-login">登 录</button>
    </form>

    <!-- 管理员登录表单 -->
    <form id="admin-login-form" action="login" method="post" style="display:none;">
        <input type="hidden" name="loginType" value="admin">
        <div class="form-group">
            <label for="adminUsername">用户名</label>
            <input
                    type="text"
                    id="adminUsername"
                    name="adminUsername"
                    class="form-control"
                    required
                    placeholder="请输入管理员用户名"
            >
        </div>

        <div class="form-group">
            <label for="adminPassword">密码</label>
            <input
                    type="password"
                    id="adminPassword"
                    name="adminPassword"
                    class="form-control"
                    required
                    placeholder="请输入管理员密码"
            >
        </div>

        <button type="submit" class="btn-login">登 录</button>
    </form>

    <div class="footer-text">
        <small>©2025 图书管理系统 v2.0</small>
    </div>
    <div class="register-link">
        没有账户？<a href="register.jsp">立即注册</a>
    </div>
</div>
</body>
</html>