package com.oyyo.gmall.search.listener;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 监听goods
 */
@Component
public class GoodsListener {

    @RabbitListener(bindings =@QueueBinding(
         value = @Queue(value = "${item.rabbitmq.queuevalue}",durable = "true"),
         exchange = @Exchange(value = "${item.rabbitmq.exchange}",type = ExchangeTypes.TOPIC,ignoreDeclarationExceptions = "true"),
         key = {"${item.rabbitmq.routingKey}"}
    ))
    public void listenerGoods(Long spuId){

    }
}
