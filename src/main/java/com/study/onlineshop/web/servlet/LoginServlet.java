package com.study.onlineshop.web.servlet;

import com.study.onlineshop.security.SecurityService;
import com.study.onlineshop.security.Session;
import com.study.onlineshop.web.templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class LoginServlet extends HttpServlet {
    private List<String> activeTokens;
    private SecurityService securityService;

    public LoginServlet(List<String> activeTokens) {
        this.activeTokens = activeTokens;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PageGenerator pageGenerator = PageGenerator.instance();
        HashMap<String, Object> parameters = new HashMap<>();

        String page = pageGenerator.getPage("login", parameters);
        resp.getWriter().write(page);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        System.out.println(login + " : " + password);

        Session session = securityService.login(login, password);
        if (session != null) {
            Cookie cookie = new Cookie("user-token", session.getToken());
            cookie.setMaxAge(60 * 60 * 5);
            resp.addCookie(cookie);
            resp.sendRedirect("/");
        } else {
            resp.sendRedirect("/login");
        }
    }
}

// id, login, password, role

// UI -> (login, password) Server -> query DB

// (trubintolik@gmail.com, 12345)

// register -> login + password -> save to db login + sha1(password)
// login -> login + password -> login + sha1(password) -> query from db

// sole = 'db2_onlineshop'
// register -> login + password -> save to db login + sha1(password + sole)
// login -> login + password -> login + sha1(password + sole) -> query from db


// sole = random -> UUID.randomUUID().toString()
// user (id, login, password, sole, userRole)
// register -> login + password -> save to db login + sha1(password + sole)
// login -> login + password -> login + sha1(password + sole) -> query from db
