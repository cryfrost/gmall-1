package com.oyyo.gmall.cart.listener;

import com.oyyo.core.bean.Resp;
import com.oyyo.gmall.cart.feign.GmallPmsClient;
import com.oyyo.gmall.pms.entity.SkuInfoEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private static final String KEY_PREFIX = "gmall:cart:";
    /**
     * 同步价格
     * @param spuId
     */
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
            log.info("同步spuId为:[{}] 的价格:[{}]", spuId,skuInfoEntity.getPrice());
            redisTemplate.opsForValue().set(PRICE_PREFIX + skuInfoEntity.getSkuId(),skuInfoEntity.getPrice().toString());
        });
        log.info("同步完成");
    }

    /**
     * 删除购物车
     * @param map
     */

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${cart.rabbitmq.queuevalueDel}",durable = "true"),
            exchange = @Exchange(value = "${cart.rabbitmq.exchangeOrder}",type = ExchangeTypes.TOPIC,ignoreDeclarationExceptions = "true"),
            key = {"${cart.rabbitmq.routingKeyDel}"}
    ))
    @RabbitHandler
    public void deleteCartListener(Map<String, Object> map){
        Long userId = (Long) map.get("userId");
        List<Object> skuIds = (List<Object>) map.get("skuIds");
        log.info("删除用户 id=[{}] 的购物车 skuIds=[{}]",userId,skuIds.toString());
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(KEY_PREFIX + userId);
        List<String> skus = skuIds.stream().map(skuId -> skuId.toString()).collect(Collectors.toList());
        String[] ids = skus.toArray(new String[skus.size()]);

        Long deleteFlag = hashOps.delete(ids);
        log.info("删除了 [{}] 条",deleteFlag);
    }
}