package com.oyyo.gmall.oms.listener;

import com.oyyo.gmall.oms.service.OrderService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 关单监听
 */
@Component
public class OrderListener {
    private static final String EXCHANGE = "GMALL-ORDER-EXCHANGE";
    private static final String ROUTINGKEYUNLOCK = "stok.unlock";
    @Autowired
    private OrderService orderService;
    @Autowired
    private AmqpTemplate amqpTemplate;

    @RabbitListener(queues = {"ORDER_DEAD_QUEUE"})
    @RabbitHandler
    public void closeOrder(String orderToken){
        int result = orderService.closeOrder(orderToken);
        //如果关单成功
        if (result == 1) {
            //发送消息解锁库存
            amqpTemplate.convertAndSend(EXCHANGE,ROUTINGKEYUNLOCK,orderToken);
        }
    }

}
