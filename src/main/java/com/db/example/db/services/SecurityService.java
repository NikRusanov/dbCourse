package com.db.example.db.services;

public interface SecurityService {
    String findLoggedInUserName();

    void autoLogin(String userName, String password);
}
