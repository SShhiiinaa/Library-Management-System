<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>图书馆后台管理</title>
    <link rel="stylesheet" href="css/admin-style.css">
    <style>
        :root {
            --primary-color: #2c3e50;
            --secondary-color: #3498db;
            --accent-color: #2ecc71;
            --danger-color: #e74c3c;
            --light-bg: #f4f6f8;
            --card-bg: #fff;
            --card-radius: 18px;
            --card-shadow: 0 6px 28px rgba(44,62,80,0.08);
            --transition: 0.22s cubic-bezier(.62,.04,.25,1);
        }
        body {
            font-family: 'Segoe UI', 'Microsoft YaHei', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(120deg, #e0eafc 0%, #cfdef3 100%);
            margin: 0;
            padding: 0;
            min-height: 100vh;
        }
        .container {
            max-width: 1080px;
            margin: 40px auto 0 auto;
            padding: 0 20px 40px 20px;
            background: rgba(255,255,255,0.97);
            border-radius: 22px;
            box-shadow: 0 9px 36px rgba(44,62,80,0.11);
        }
        h2 {
            text-align: center;
            color: var(--secondary-color);
            font-size: 2.2rem;
            font-weight: 900;
            letter-spacing: 2px;
            margin-top: 0;
            padding-top: 30px;
            margin-bottom: 34px;
            text-shadow: 0 3px 22px #b8d6f5;
            user-select: none;
        }
        .section {
            background: var(--card-bg);
            border-radius: var(--card-radius);
            box-shadow: var(--card-shadow);
            padding: 32px 36px 24px 36px;
            margin-top: 30px;
            margin-bottom: 30px;
            transition: box-shadow var(--transition), transform var(--transition);
            position: relative;
        }
        .section:hover {
            box-shadow: 0 16px 48px rgba(44,62,80,0.18);
            transform: translateY(-4px) scale(1.022);
        }
        .section h3 {
            margin-bottom: 18px;
            color: var(--primary-color);
            font-size: 1.25rem;
            letter-spacing: 1px;
            font-weight: 700;
            position: relative;
        }
        .section h3:before {
            content: "";
            display: inline-block;
            vertical-align: middle;
            width: 7px;
            height: 23px;
            border-radius: 5px;
            margin-right: 10px;
            background: linear-gradient(120deg, var(--secondary-color) 50%, var(--accent-color) 100%);
        }
        ul {
            list-style: none;
            padding-left: 0;
            margin-bottom: 0;
            display: flex;
            flex-wrap: wrap;
            gap: 16px 24px;
        }
        ul li {
            margin: 0;
        }
        ul li a {
            display: inline-block;
            min-width: 120px;
            padding: 10px 26px;
            background: linear-gradient(90deg, #e6f0fa 10%, #f2faf6 100%);
            border-radius: 30px;
            font-size: 1.07em;
            color: var(--secondary-color);
            font-weight: 500;
            text-decoration: none;
            box-shadow: 0 2px 8px rgba(52,152,219,0.06);
            border: 1px solid #e4ecf5;
            transition: background var(--transition), color var(--transition), transform 0.12s;
            position: relative;
        }
        ul li a:before {
            content: '';
            display: inline-block;
            vertical-align: middle;
            width: 7px;
            height: 7px;
            border-radius: 50%;
            background: var(--accent-color);
            margin-right: 7px;
        }
        ul li a:hover, ul li a:active {
            background: linear-gradient(90deg, var(--secondary-color) 30%, var(--accent-color) 100%);
            color: #fff;
            border: 1px solid var(--accent-color);
            filter: drop-shadow(0 6px 16px #b3e5fc66);
            transform: translateY(-2px) scale(1.06);
        }
        .btn {
            padding: 9px 24px;
            border: none;
            background: linear-gradient(90deg, var(--accent-color), var(--secondary-color));
            color: white;
            border-radius: 18px;
            text-decoration: none;
            font-weight: bold;
            box-shadow: 0 2px 7px #b8d6f5;
            letter-spacing: 1px;
            font-size: 1.07em;
            margin-top: 8px;
        }
        .btn:hover {
            background: linear-gradient(90deg, var(--secondary-color), var(--accent-color));
            box-shadow: 0 6px 18px #b8d6f5;
        }
        @media (max-width: 700px) {
            .container {
                padding: 0 2vw 20px 2vw;
            }
            .section {
                padding: 16px 8px 16px 8px;
            }
            ul { gap: 10px 7px;}
            ul li a { padding: 8px 7px; min-width: 90px;}
            h2 { font-size: 1.3rem;}
        }
    </style>
</head>
<body>
<div class="container">
    <h2>📚 图书馆后台管理系统</h2>

    <!-- 图书管理模块 -->
    <div class="section">
        <h3>图书管理</h3>
        <ul>
            <li><a href="bookManage.jsp">查看图书列表</a></li>
            <li><a href="book_add.jsp">➕ 新增图书</a></li>
            <li><a href="category_list.jsp">分类管理</a></li>
            <li><a href="publisher_list.jsp">出版社管理</a></li>
        </ul>
    </div>

    <!-- 用户管理模块 -->
    <div class="section">
        <h3>用户管理</h3>
        <ul>
            <li><a href="user_list.jsp">查看所有用户</a></li>
            <li><a href="user_borrow_status.jsp">用户借阅状态</a></li>
            <li><a href="user_reset_password.jsp">重置用户密码</a></li>
        </ul>
    </div>

    <!-- 借阅管理模块 -->
    <div class="section">
        <h3>借阅管理</h3>
        <ul>
            <li><a href="borrow_list.jsp">借阅记录</a></li>
            <li><a href="borrow_add.jsp">手动添加借阅记录</a></li>
            <li><a href="return_add.jsp">手动添加还书记录</a></li>
            <li><a href="overdue_notify.jsp">催还书提醒</a></li>
        </ul>
    </div>

    <!-- 系统配置 -->
    <div class="section">
        <h3>系统设置</h3>
        <ul>
            <li><a href="announcement.jsp">系统公告管理</a></li>
            <li><a href="config.jsp">借阅配置（最大本数/时长）</a></li>
            <li><a href="admin_list.jsp">管理员列表</a></li>
            <li><a href="admin_add.jsp">添加新管理员</a></li>
        </ul>
    </div>
</div>
</body>
</html>