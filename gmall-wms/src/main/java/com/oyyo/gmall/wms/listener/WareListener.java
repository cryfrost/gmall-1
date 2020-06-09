package com.oyyo.gmall.wms.listener;

import com.alibaba.fastjson.JSON;
import com.oyyo.gmall.wms.dao.WareSkuDao;
import com.oyyo.gmall.wms.vo.SkuLockVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 监听库存解锁
 */
@Slf4j
@Component
public class WareListener {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private WareSkuDao wareSkuDao;
    private static final String KEY_PREFIX = "stock:lock";

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${order.rabbitmq.queue}"),
            exchange = @Exchange(value = "${order.rabbitmq.exchange}",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = {"${order.rabbitmq.routingKeyUnlock}"}
    ))
    @RabbitHandler
    public void unLockListener(String orderToken) {
        String jsonOrderToken = redisTemplate.opsForValue().get(KEY_PREFIX + orderToken);
        log.info("解锁库存,orderToken=[{}]",orderToken);

        List<SkuLockVO> skuLockVOS = JSON.parseArray(jsonOrderToken, SkuLockVO.class);
        skuLockVOS.forEach(skuLockVO -> {
            wareSkuDao.unLockStore(skuLockVO.getWareSkuId(),skuLockVO.getCount());
        });
        log.info("解锁库存完成,orderToken=[{}]",orderToken);
    }
}
