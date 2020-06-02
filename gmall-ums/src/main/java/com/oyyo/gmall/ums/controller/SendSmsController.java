package com.oyyo.gmall.ums.controller;

import com.oyyo.core.bean.Resp;
import com.oyyo.gmall.ums.service.SendSmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendSmsController {

    @Autowired
    private SendSmsService sendSmsService;

    /**
     * 发送注册短信验证码
     * @param phone
     * @return
     */
    @PostMapping("code")
    public Resp<String> sendSms(@RequestParam("phone")String phone){

        String reslut = sendSmsService.sendSms(phone);
        return Resp.ok(reslut);
    }
}
