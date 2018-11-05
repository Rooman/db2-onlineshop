package com.study.onlineshop.security;

import com.study.onlineshop.entity.User;
import com.study.onlineshop.service.UserService;

import javax.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.util.*;

public class SecurityService {
    private List<Session> sessionList = new ArrayList<>();

    private UserService userService;

    public Session login(String login, String password) {
        User user = userService.getUser(login, password);
        if (user != null) {
            Session session = new Session();
            session.setUser(user);
            session.setToken(UUID.randomUUID().toString());

            session.setExpireDate(LocalDateTime.now().plusHours(15));
            sessionList.add(session);
            return session;
        }
        return null;
    }

    public void logout(String token) {
        for (Session session : sessionList) {
            if (session.getToken().equals(token)) {
                sessionList.remove(session);
                break;
            }
        }
    }

    // Optional
    public Optional<Session> getSession(String token) {
        for (Session session : sessionList) {
            if (session.getToken().equals(token)) {
                if (session.getExpireDate().isBefore(LocalDateTime.now())) {
                    sessionList.remove(session);
                    return Optional.empty();
                }
                return Optional.of(session);
            }
        }
        return Optional.empty();
    }

//    public Session getSession(String token) {
//        for (Session session : sessionList) {
//            if (session.getToken().equals(token)) {
//                if (session.getExpireDate().isBefore(LocalDateTime.now())) {
//                    sessionList.remove(session);
//                    return null;
//                }
//                return session;
//            }
//        }
//        return null;
//    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public String getValidatedToken(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("user-token")) {
                    String token = cookie.getValue();
                    if (token != null && !token.isEmpty()) {
                        Session session = getSession(token);
                        if (session != null) {
                            return token;
                        }
                    }
                    break;
                }
            }
        }
        return null;
    }

    public boolean checkTokenPermissions(String token, EnumSet roles) {
        Session session = getSession(token);
        return session != null && roles.contains(session.getUser().getUserRole());
    }

}
