package com.study.onlineshop.web.servlet;

import com.study.onlineshop.security.SecurityService;
import com.study.onlineshop.security.Session;
import com.study.onlineshop.service.UserService;
import com.study.onlineshop.web.templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class RegisterServlet extends HttpServlet {
    private SecurityService securityService;
    private UserService userService;

    public RegisterServlet(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PageGenerator pageGenerator = PageGenerator.instance();
        HashMap<String, Object> parameters = new HashMap<>();

        String page = pageGenerator.getPage("register", parameters);
        response.getWriter().write(page);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String verifyPassword = request.getParameter("verify_password");
        System.out.println(login + " : " + password + " : " + verifyPassword);

        if (password != null && !password.isEmpty() && password.equals(verifyPassword)) {
            userService.add(login, password);
            Session session = securityService.login(login, password);
            if (session != null) {
                Cookie cookie = new Cookie("user-token", session.getToken());
                cookie.setMaxAge(60 * 60 * 5);
                response.addCookie(cookie);
                response.sendRedirect("/");
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } else {
            response.sendRedirect("/register");
        }
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
