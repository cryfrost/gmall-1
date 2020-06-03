package com.oyyo.gmall.auth.controller;

import com.oyyo.core.bean.Resp;
import com.oyyo.gmall.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: AuthController
 * @Description: TODO
 * @Author: LiKui
 * @Date: 2020-6-3 17:56
 * @Version: 1.0
 */
@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * token 授权
     * @param username
     * @param password
     * @return
     */
    @PostMapping("accredit")
    public Resp<Boolean> accredit(@RequestParam("username")String username,@RequestParam("password")String password){
        Boolean result = authService.accredit(username,password);
        return Resp.ok(result);
    }
}