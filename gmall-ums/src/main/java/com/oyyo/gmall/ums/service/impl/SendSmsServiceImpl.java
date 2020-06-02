package com.oyyo.gmall.ums.service.impl;

import com.oyyo.gmall.ums.service.SendSmsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SendSmsServiceImpl implements SendSmsService {

    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private StringRedisTemplate redisTemplate;
    //队列名
    @Value("${sms.rabbitmq.exchange}")
    private String EXCHANGE_NAME;
    //路由key
    @Value("${sms.rabbitmq.routingKey}")
    private String ROUTINGKEY;

    private static final String KEY_PREFIX = "user:verity";

    @Override
    public String sendSms(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return "error";
        }
        //先查询缓存中是否存在
        String result = redisTemplate.opsForValue().get(KEY_PREFIX + phone);
        //如果存在 则返回false 前台拿到数据判断 --说明3分钟之内已经发送过短信
        if (StringUtils.isNotBlank(result)) {
            log.info("三分钟之内发送过短信验证码，" + result);
            return "false";
        }
        //生成验证吗
        String verifyCode = getVerifyCode();
        log.info("生成的验证吗为:" + verifyCode);
        //发送消息到消息队列
        log.info("开始发送消息");
        Map<String, String> msg = new HashMap<>();
        msg.put("phone",phone);
        // type: 1:注册验证吗 2：待定
        msg.put("type","1");
        msg.put("code",verifyCode);
        log.info("消息参数为：" + msg.toString());
        sendMsg("1",msg);

        // 缓存验证吗

        log.info("放入缓存");
        redisTemplate.opsForValue().set( KEY_PREFIX + phone, verifyCode,3, TimeUnit.MINUTES);
        return "true";
    }

    /**
     * 发送消息至消息队列
     * @param type
     * @param verifyCode
     */
    public void sendMsg(String type,Map<String,String> verifyCode){
        log.info("发送消息开始 msg为：" + verifyCode + "__type为：" + type);
        amqpTemplate.convertAndSend(EXCHANGE_NAME,ROUTINGKEY + type,verifyCode);

        log.info("发送消息结束 EXCHANGE_NAME：" + EXCHANGE_NAME + "__ROUTINGKEY：" + ROUTINGKEY + "__type为：" + type);

    }

    /**
     * 生成6位短信验证吗
     * @return
     */
    public static String getVerifyCode() {
        return (int) ((Math.random() * 9 + 1) * 100000)+"";
    }

}
