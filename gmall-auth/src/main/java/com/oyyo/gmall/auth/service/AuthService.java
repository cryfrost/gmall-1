package com.oyyo.gmall.auth.service;

public interface AuthService {
    /**
     * 授权token
     * @param username
     * @param password
     * @return
     */
    Boolean accredit(String username, String password);
}
