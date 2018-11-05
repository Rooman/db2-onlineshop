package com.study.onlineshop.web.servlet;

import com.study.onlineshop.security.SecurityService;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutServlet extends HttpServlet {
    private SecurityService securityService;

    public LogoutServlet(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie: request.getCookies()) {
                if("user-token".equals(cookie.getName())){
                    securityService.logout(cookie.getValue());
                    cookie.setMaxAge(-1);
                }
            }
        }
        response.sendRedirect("/login");
    }
}

