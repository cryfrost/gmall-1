package com.oyyo.gmall.cart.listener;

import com.oyyo.core.bean.Resp;
import com.oyyo.gmall.cart.feign.GmallPmsClient;
import com.oyyo.gmall.pms.entity.SkuInfoEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName: 哦同步价格
 * @Description: TODO
 * @Author: LiKui
 * @Date: 2020-6-5 13:25
 * @Version: 1.0
 */
@Component
@Slf4j
public class CartListener {

    @Autowired
    private GmallPmsClient pmsClient;
    @Autowired
    private StringRedisTemplate redisTemplate;
    private static final String PRICE_PREFIX = "gmall:sku:";

    @RabbitListener(bindings =@QueueBinding(
            value = @Queue(value = "${cart.rabbitmq.queuevalue}",durable = "true"),
            exchange = @Exchange(value = "${cart.rabbitmq.exchange}",type = ExchangeTypes.TOPIC,ignoreDeclarationExceptions = "true"),
            key = {"${cart.rabbitmq.routingKey}"}
    ))
    @RabbitHandler
    public void listenerPmsPrice(Long spuId){
        //获取消息队列中的 spuId
        log.info("消息队列中的spuId为:[{}]", spuId);
        Resp<List<SkuInfoEntity>> skusResp = pmsClient.querySkusBySpuId(spuId);
        List<SkuInfoEntity> skus = skusResp.getData();
        skus.forEach(skuInfoEntity -> {
            log.info("同步skuId为:[{}] 的价格", spuId);
            redisTemplate.opsForValue().set(PRICE_PREFIX + skuInfoEntity.getSkuId(),skuInfoEntity.getPrice().toString());
        });
        log.info("同步完成");

    }
}