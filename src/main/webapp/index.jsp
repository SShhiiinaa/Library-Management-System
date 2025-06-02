<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>图书管理系统 - 首页</title>
    <style>
        :root {
            --main-dark: #18181A;       /* 主色 深灰黑 60% */
            --purple: #7C3AED;          /* 辅色 亮紫 30% */
            --neon-green: #06D6A0;      /* 辅色 荧光绿 9% */
            --offwhite: #F7F6EF;        /* 点缀色（米白，替代明黄） 1% */
            --bg-light: #F8F9FA;        /* 背景 */
            --white: #fff;
            --card-radius: 20px;
            --card-shadow: 0 6px 28px rgba(28,32,48,0.09);
            --transition: 0.22s cubic-bezier(.62,.04,.25,1);
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: var(--bg-light);
            color: var(--main-dark);
            margin: 0;
            min-height: 100vh;
        }
        .header {
            background: linear-gradient(90deg, var(--main-dark) 70%, var(--purple) 100%);
            color: var(--offwhite);
            padding: 2.2rem 0 1.5rem 0;
            box-shadow: 0 2px 18px rgba(28,32,48,0.12);
            border-bottom-left-radius: 32px;
            border-bottom-right-radius: 32px;
            display: flex;
            flex-direction: column;
            align-items: center;
        }
        .main-title {
            font-size: 2.7rem;
            font-weight: 800;
            letter-spacing: 0.08em;
            color: var(--offwhite);
            text-shadow:
                    0 2px 24px var(--purple),
                    0 0px 5px var(--main-dark),
                    0 0px 2px var(--offwhite);
            margin-bottom: 14px;
            user-select: none;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .main-title:hover {
            letter-spacing: 0.14em;
            color: var(--neon-green);
            text-shadow:
                    0 6px 32px var(--neon-green), 0 1px 0 var(--purple);
        }
        .user-block {
            margin-top: 0.6rem;
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 9px;
        }
        .logout-btn {
            background: var(--purple);
            color: var(--main-dark);
            border: none;
            padding: 0.7rem 2.1rem;
            border-radius: 28px;
            font-size: 1rem;
            font-weight: bold;
            cursor: pointer;
            box-shadow: 0 3px 9px #7C3AED22;
            transition: background 0.18s, color 0.18s, transform 0.15s;
        }
        .logout-btn:hover {
            background: var(--neon-green);
            color: var(--main-dark);
            transform: scale(1.07);
        }
        .user-info {
            padding: 0.60em 2em;
            background: rgba(247,246,239,0.21);
            border-radius: 20px;
            font-size: 1.08em;
            font-weight: 500;
            color: var(--offwhite);
            letter-spacing: 1px;
            box-shadow: 0 1px 8px #7C3AED22;
        }
        .top-nav {
            width: 100%;
            max-width: 1200px;
            display: flex;
            justify-content: flex-end;
            gap: 1.3rem;
            margin: 0 auto;
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
        .welcome-section {
            background: linear-gradient(90deg, var(--offwhite) 60%, var(--purple) 100%);
            max-width: 1100px;
            margin: 2.5rem auto 2.2rem auto;
            border-radius: 18px;
            box-shadow: 0 2px 16px #18181A11;
            padding: 2rem 0;
            text-align: center;
        }
        .welcome-section h2 {
            font-size: 2.1rem;
            color: var(--main-dark);
            margin-bottom: 0.7rem;
            font-weight: bold;
            letter-spacing: 2px;
        }
        .dashboard-grid {
            max-width: 1200px;
            margin: auto;
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(295px, 1fr));
            gap: 2.1rem;
            padding: 0 12px 40px 12px;
        }
        .card {
            background: linear-gradient(135deg, var(--white) 80%, var(--purple) 120%);
            border-radius: var(--card-radius);
            padding: 2rem 1.4rem 1.4rem 1.4rem;
            box-shadow: var(--card-shadow);
            transition: transform .19s, box-shadow .22s, border-color .22s;
            cursor: pointer;
            text-decoration: none;
            color: var(--main-dark);
            border: 2.5px solid var(--bg-light);
            min-height: 180px;
            position: relative;
            overflow: hidden;
        }
        .card-admin {
            border-left: 8px solid var(--purple);
        }
        .card-user {
            border-left: 8px solid var(--neon-green);
        }
        .card:before {
            content: '';
            position: absolute;
            top: -48px;
            right: -48px;
            width: 90px;
            height: 90px;
            background: linear-gradient(135deg, var(--main-dark) 0%, var(--offwhite) 100%);
            border-radius: 50%;
            opacity: 0.13;
            z-index: 0;
            transition: opacity 0.3s;
        }
        .card h3 {
            margin: 0 0 1rem 0;
            color: var(--purple);
            font-size: 1.35rem;
            font-weight: 800;
            letter-spacing: 1px;
            z-index: 1;
            position: relative;
        }
        .card p {
            color: #3f3f45;
            line-height: 1.7;
            font-size: 1.04rem;
            z-index: 1;
            position: relative;
        }
        .card:hover {
            transform: translateY(-8px) scale(1.04);
            box-shadow: 0 8px 32px #7C3AED33;
            border-color: var(--neon-green);
        }
        .card:active {
            transform: scale(0.98);
        }
        .card:hover:before { opacity: 0.22; }
        .tips-section {
            background: linear-gradient(95deg, var(--neon-green) 20%, var(--offwhite) 80%);
            max-width: 1100px;
            margin: 35px auto 0 auto;
            border-radius: 16px;
            box-shadow: 0 1px 12px #18181A0c;
            color: var(--main-dark);
            padding: 1.25rem 2rem 1.1rem 2rem;
            font-size: 1.03rem;
            display: flex;
            align-items: center;
            gap: 14px;
        }
        .tips-section i {
            color: var(--purple);
            font-size: 1.3rem;
        }
        .ripple {
            position: absolute;
            border-radius: 50%;
            transform: scale(0);
            animation: ripple 0.45s linear;
            background-color: #7C3AED22;
            z-index: 2;
            pointer-events: none;
        }
        @keyframes ripple {
            to {
                transform: scale(4);
                opacity: 0;
            }
        }
        @media (max-width: 700px) {
            .header {
                padding: 1.1rem 0 1rem 0;
                border-radius: 0 0 20px 20px;
            }
            .container {
                margin-top: 1.2rem;
            }
            .welcome-section h2 {
                font-size: 1.2rem;
            }
            .card { padding: 1.2rem 0.7rem 1rem 0.7rem;}
            .tips-section {
                font-size: 0.93rem;
                padding: 0.8rem 1rem;
            }
        }
    </style>
</head>
<body>
<div class="header">
    <div class="top-nav">
        <a class="nav-link" href="search.jsp">图书查询</a>
        <c:if test="${sessionScope.user.role == 'ADMIN'}">
            <a class="nav-link" href="bookManage.jsp">图书管理</a>
            <a class="nav-link" href="readerManage">用户管理</a>
        </c:if>
        <c:if test="${sessionScope.user.role == 'USER'}">
            <a class="nav-link" href="borrowRecord">我的借阅</a>
            <a class="nav-link" href="borrow.jsp">图书借阅</a>
            <a class="nav-link" href="returnBooks.jsp">图书归还</a>
        </c:if>
    </div>
    <h1 class="main-title">图书管理系统</h1>
    <c:if test="${not empty sessionScope.user}">
        <div class="user-block">
            <button class="logout-btn"
                    onclick="location.href='${pageContext.request.contextPath}/logout'">
                退出登录
            </button>
            <div class="user-info">
                当前用户:
                <span style="font-weight:bold;">
                        ${not empty sessionScope.user ? sessionScope.user.username : '未知用户'}
                </span>
                &nbsp;
                <span style="color: var(--offwhite);">
                    (${not empty sessionScope.role
                        ? (sessionScope.role == 'ADMIN' ? '管理员' : '普通用户')
                        : '未授权用户'})
                </span>
            </div>
            <c:if test="${sessionScope.user.role == 'USER' && not empty sessionScope.user}">
                <div style="font-size:1.06em; margin-top:6px;">
                    账户状态：
                    <c:choose>
                        <c:when test="${sessionScope.user.status == 'A'}">
                            <span style="color:#06D6A0;">正常</span>
                        </c:when>
                        <c:otherwise>
                            <span style="color:#F44;">已封禁</span>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>
        </div>
    </c:if>
</div>

<div class="welcome-section">
    <h2>欢迎使用图书管理系统</h2>
</div>

<div class="dashboard-grid">
    <!-- 公共功能 -->
    <a href="search.jsp" class="card">
        <h3>📚 图书查询</h3>
        <p>通过书名、作者或ISBN检索馆藏图书</p>
    </a>

    <!-- 管理员专属功能 -->
    <c:if test="${sessionScope.user.role == 'ADMIN'}">
        <a href="bookManage.jsp" class="card card-admin">
            <h3>📦 图书管理</h3>
            <p>新增/编辑图书信息，管理库存</p>
        </a>
        <a href="readerManage" class="card card-admin">
            <h3>👥 用户管理</h3>
            <p>管理系统用户账户与权限</p>
        </a>
    </c:if>

    <!-- 用户功能 -->
    <c:if test="${sessionScope.user.role == 'USER'}">
        <a href="borrowRecord" class="card card-user">
            <h3>📖 我的借阅</h3>
            <p>查看当前借阅记录与历史记录</p>
        </a>
        <a href="borrow.jsp" class="card card-user">
            <h3>📚 图书借阅</h3>
            <p>在线搜索感兴趣的书籍并提交借阅申请</p>
        </a>
        <a href="returnBooks" class="card card-user">
            <h3>🔄 图书归还</h3>
            <p>管理和归还当前已借阅但未归还的图书</p>
        </a>
    </c:if>
</div>



<script>
    // 卡片点击波纹特效
    document.querySelectorAll('.card').forEach(card => {
        card.addEventListener('click', function(e) {
            let ripple = document.createElement('span');
            ripple.className = 'ripple';
            let rect = card.getBoundingClientRect();
            ripple.style.width = ripple.style.height = Math.max(rect.width, rect.height) + 'px';
            ripple.style.left = (e.clientX - rect.left - rect.width / 2) + 'px';
            ripple.style.top = (e.clientY - rect.top - rect.height / 2) + 'px';
            card.appendChild(ripple);
            setTimeout(() => ripple.remove(), 500);
        });
    });
</script>
</body>
</html>