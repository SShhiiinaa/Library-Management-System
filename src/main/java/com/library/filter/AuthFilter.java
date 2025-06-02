package com.library.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    // 需要管理员权限的路径前缀
    private static final String[] ADMIN_PATHS = {"/admin"};

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String path = request.getRequestURI().substring(request.getContextPath().length());

        // 允许公共资源访问
        if (isPublicPath(path)) {
            chain.doFilter(request, response);
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isPublicPath(String path) {
        return path.startsWith("/login") ||        // 登录相关
                path.startsWith("/static") ||       // 静态资源
                path.equals("/register.jsp")
                ||path.equals("/borrow.jsp");
    }

    private boolean isAdminPath(String path) {
        return path.equals("/userManage") ||
                path.startsWith("/admin/");
    }
}