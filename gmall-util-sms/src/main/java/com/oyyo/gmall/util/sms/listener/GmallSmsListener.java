package com.oyyo.gmall.util.sms.listener;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.CommonResponse;
import com.oyyo.gmall.util.sms.utils.CommonSendSmsUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: GmallSmsAspect
 * @Description: TODO
 * @Author: LiKui
 * @Date: 2020-6-2 14:17
 * @Version: 1.0
 */
@Component
@Slf4j
public class GmallSmsListener {

    @Autowired
    private CommonSendSmsUtils sendSmsUtils;

    @Value("#{${sms.templateCode}}")
    private Map<String, String> templateCode;

    @RabbitListener(bindings =@QueueBinding(
            value = @Queue(value = "${sms.rabbitmq.queuevalue}",durable = "true"),
            exchange = @Exchange(value = "${sms.rabbitmq.exchange}",type = ExchangeTypes.TOPIC,ignoreDeclarationExceptions = "true"),
            key = {"${sms.rabbitmq.routingKey}"}
    ))
    @RabbitHandler
    public void listenerSms(Map<String, String> msg){

        if (CollectionUtils.isEmpty(msg)) {
            return;
        }

        String phone = msg.get("phone");
        String code = msg.get("code");
        String type = msg.get("type");
        String templateCodeDesc;
        //判断发送验证码类型
        if (StringUtils.isNoneBlank(phone,code,type)) {
            switch (type) {
                case "1":
                    //注册验证码
                    templateCodeDesc = templateCode.get("registerCode");
                    break;
                default:
                    log.info("短信类型错误！type=【{}】",type);
                    return;
            }
            Map<String,String> verifyCode = new HashMap<>();
            verifyCode.put("code",code);
            CommonResponse commonResponse = sendSmsUtils.sendSms(phone, templateCodeDesc, JSON.toJSONString(verifyCode));
            log.info("短信发送返回结果:{}",commonResponse.getData());
        }


    }
}