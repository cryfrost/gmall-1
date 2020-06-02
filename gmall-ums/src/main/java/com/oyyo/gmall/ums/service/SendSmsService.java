package com.oyyo.gmall.ums.service;

public interface SendSmsService {

    /**
     * 发送短信验证吗
     * @param phone
     * @return
     */
    String sendSms(String phone);
}
