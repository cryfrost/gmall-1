package com.oyyo.gmall.auth.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthService {
    /**
     * 授权token
     * @param username
     * @param password
     * @param request
     * @param response
     * @return
     */
    Boolean accredit(String username, String password, HttpServletRequest request, HttpServletResponse response);
}
