<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>我的借阅记录</title>
    <style>
        :root {
            --primary-color: #2c3e50;
            --secondary-color: #3498db;
            --success-color: #2ecc71;
            --error-color: #e74c3c;
            --warning-color: #f39c12;
            --border-radius: 12px;
            --shadow: 0 2px 18px rgba(44,62,80,0.10);
            --table-bg: #f7fafc;
            --main-dark: #18181A;
            --purple: #7C3AED;
            --neon-green: #06D6A0;
            --offwhite: #F7F6EF;
        }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            background: linear-gradient(120deg, #e0eafc 0%, #cfdef3 100%);
            min-height: 100vh;
            animation: fadein 0.9s;
        }
        @keyframes fadein {
            from { opacity: 0;}
            to { opacity: 1;}
        }
        .header {
            background: linear-gradient(90deg, var(--main-dark) 70%, var(--purple) 100%);
            color: var(--offwhite);
            padding: 1.2rem 0 1rem 0;
            box-shadow: 0 2px 18px rgba(28,32,48,0.12);
            border-bottom-left-radius: 32px;
            border-bottom-right-radius: 32px;
            display: flex;
            flex-direction: column;
            align-items: center;
        }
        .top-nav {
            width: 100%;
            max-width: 1200px;
            display: flex;
            justify-content: flex-end;
            gap: 1.3rem;
            margin: 0 auto;
            position: relative;
        }
        .nav-link {
            color: var(--offwhite);
            background: transparent;
            padding: 0.45em 1.2em;
            border-radius: 16px;
            font-weight: 500;
            text-decoration: none;
            font-family: inherit;
            transition: background 0.15s, color 0.15s;
        }
        .nav-link:hover {
            background: var(--neon-green);
            color: var(--main-dark);
        }
        .home-btn {
            color: var(--main-dark);
            background: var(--neon-green);
            padding: 0.45em 1.2em;
            border-radius: 16px;
            font-weight: 500;
            text-decoration: none;
            font-family: inherit;
            margin-right: auto;
            margin-left: 0;
            border: none;
            cursor: pointer;
            transition: background 0.15s, color 0.15s, transform 0.15s;
            display: flex;
            align-items: center;
            gap: 0.4em;
        }
        .home-btn:hover {
            background: var(--secondary-color);
            color: #fff;
            transform: translateY(-2px) scale(1.05);
        }
        .container {
            max-width: 1100px;
            margin: 36px auto;
            background: rgba(255,255,255,0.97);
            border-radius: var(--border-radius);
            box-shadow: var(--shadow);
            padding: 36px 32px 28px 32px;
        }
        h1 {
            text-align: center;
            color: var(--secondary-color);
            font-size: 2.3rem;
            font-weight: 800;
            letter-spacing: 2px;
            margin-top: 0;
            margin-bottom: 2.2rem;
            text-shadow: 0 2px 12px #b8d6f5;
            user-select: none;
        }
        .error-message {
            text-align: center;
            color: var(--error-color);
            background: #ffecec;
            border-radius: 4px;
            padding: 1rem;
            margin-bottom: 20px;
            font-weight: 600;
            font-size: 1.08rem;
        }
        .record-table {
            width: 100%;
            border-collapse: separate;
            border-spacing: 0;
            background: var(--table-bg);
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 1px 8px #b8d6f5;
            animation: fadein 1.2s;
        }
        .record-table th, .record-table td {
            padding: 16px 12px;
            text-align: left;
        }
        .record-table th {
            background: linear-gradient(90deg, var(--primary-color), var(--secondary-color) 65%);
            color: white;
            font-weight: bold;
            font-size: 1.07rem;
            border: none;
        }
        .record-table tr {
            transition: background 0.2s;
        }
        .record-table tbody tr:hover {
            background: #eaf6ff;
        }
        .record-table td {
            border-bottom: 1px solid #e3e9ef;
            font-size: 1.01rem;
        }
        .record-table tr:last-child td {
            border-bottom: none;
        }
        .overdue {
            color: var(--error-color);
            font-weight: bold;
            letter-spacing: 1px;
        }
        .returned {
            color: var(--success-color);
            font-weight: bold;
        }
        .record-table em {
            color: #888;
            font-size: 0.96em;
        }
        .status-tag {
            display: inline-block;
            padding: 0.24em 1em;
            border-radius: 16px;
            font-size: 0.98em;
            font-weight: 600;
            margin-top: 2px;
            background: #ecf5ff;
            color: var(--secondary-color);
        }
        .status-tag.returned {
            background: #eafaf1;
            color: var(--success-color);
        }
        .status-tag.overdue {
            background: #fff2f2;
            color: var(--error-color);
        }
        .status-tag.ontime {
            background: #f5f7fa;
            color: #888;
        }
        .no-records {
            text-align: center;
            color: #666;
            padding: 2rem;
            font-size: 1.08rem;
        }
        @media (max-width: 750px) {
            .container { padding: 10px 2vw;}
            h1 { font-size: 1.3rem;}
            .record-table th, .record-table td { padding: 9px 2px;}
        }
    </style>
</head>
<body>
<div class="header">
    <div class="top-nav">
        <!-- 返回首页按钮放最左侧 -->
        <a class="home-btn" href="index.jsp">&larr; 返回首页</a>
        <a class="nav-link" href="search.jsp">图书查询</a>
        <c:if test="${sessionScope.user.role == 'ADMIN'}">
            <a class="nav-link" href="bookManage.jsp">图书管理</a>
            <a class="nav-link" href="userManage">用户管理</a>
        </c:if>
        <c:if test="${sessionScope.user.role == 'USER'}">
            <a class="nav-link" href="borrowRecord">我的借阅</a>
            <a class="nav-link" href="borrow.jsp">图书借阅</a>
            <a class="nav-link" href="returnBooks.jsp">图书归还</a>
        </c:if>
    </div>
</div>
<div class="container">
    <!-- 内容区不再需要返回首页按钮 -->
    <h1>📚 我的借阅记录</h1>
    <c:if test="${not empty error}">
        <div class="error-message">错误: ${error}</div>
    </c:if>
    <c:choose>
        <c:when test="${empty records}">
            <div class="no-records">暂无借阅记录</div>
        </c:when>
        <c:otherwise>
            <table class="record-table">
                <thead>
                <tr>
                    <th>图书信息</th>
                    <th>借阅日期</th>
                    <th>应还日期</th>
                    <th>归还状态</th>
                    <th>逾期天数</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${records}" var="record">
                    <tr>
                        <td>
                            <strong>${record.bookTitle}</strong><br>
                            <em>${record.bookAuthor}</em><br>
                            <span style="font-size:0.95em;color:#bbb;">ISBN: ${record.isbn}</span>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty record.borrowDate}">
                                    ${record.borrowDate}
                                </c:when>
                                <c:otherwise>
                                    <span class="status-tag ontime">未知</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty record.dueDate}">
                                    ${record.dueDate}
                                    <c:if test="${record.overdueDays > 0 && empty record.returnDate}">
                                        <span class="status-tag overdue">已逾期</span>
                                    </c:if>
                                </c:when>
                                <c:otherwise>
                                    <span class="status-tag ontime">未知</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty record.returnDate}">
                                    <span class="status-tag returned">已归还</span><br>
                                    <span style="color:#888;font-size:0.96em;">${record.returnDate}</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="status-tag">未归还</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${record.overdueDays > 0}">
                                    <span class="overdue">${record.overdueDays} 天</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="status-tag ontime">—</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>