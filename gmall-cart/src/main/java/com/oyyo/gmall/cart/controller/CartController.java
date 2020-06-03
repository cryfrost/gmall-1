package com.oyyo.gmall.cart.controller;

import com.oyyo.gmall.cart.interceptors.LoginInterceptor;
import com.oyyo.gmall.cart.vo.UserInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {

    @GetMapping("test")
    public String test(){
        UserInfo threadLoclUserInfo = LoginInterceptor.getThreadLoclUserInfo();
        return "succes";
    }
}
