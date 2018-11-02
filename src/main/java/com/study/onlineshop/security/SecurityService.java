package com.study.onlineshop.security;

import com.study.onlineshop.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SecurityService {
    private List<Session> sessionList = new ArrayList<>();

    private UserService userService;

    public Session login(String name, String password) {
        User user = userService.getUser(name, password);
        if (user != null) {
            Session session = new Session();
            session.setUser(user);
            session.setToken(UUID.randomUUID().toString());

            session.setExpireDate(LocalDateTime.now().plusHours(5));
            sessionList.add(session);
            return session;
        }

        return null;
    }

    public void logout(String token) {
        // iterate over session, find and remove session
    }

    public Session getSession(String token) {
        // iterate, check if not expired  and return
        return null;
    }
}
