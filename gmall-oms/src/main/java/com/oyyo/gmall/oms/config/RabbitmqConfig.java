package com.oyyo.gmall.oms.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitmqConfig {

    @Value("${order.rabbitmq.routingKey}")
    private static String ROUTINGKEY;

    private static final String DEAD_LETTER_EXCHANGE = "order.dead.letter.exchange";
    private static final String DEAD_LETTER_QUEUEA_ROUTING_KEY = "order.dead";
    private static final String DELAY_QUEUEA_NAME = "ORDER-TTL-QUEUE";
    private static final String ORDER_DEAD_QUEUE = "ORDER-DEAD-QUEUE";


    @Bean(DELAY_QUEUEA_NAME)
    public Queue ttlQueue(){

        Map<String, Object> args = new HashMap<>();
        // x-dead-letter-exchange    这里声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
        // x-dead-letter-routing-key  这里声明当前队列的死信路由key
        args.put("x-dead-letter-routing-key", DEAD_LETTER_QUEUEA_ROUTING_KEY);
        // x-message-ttl  声明队列的TTL
        args.put("x-message-ttl", 120000);
        return new Queue(DELAY_QUEUEA_NAME ,true,false,false,args);
    }

    @Bean(DELAY_QUEUEA_NAME)
    public Binding ttlBinding(){
        return new Binding(DELAY_QUEUEA_NAME, Binding.DestinationType.QUEUE, DEAD_LETTER_EXCHANGE, ROUTINGKEY, null);
    }

    @Bean(ORDER_DEAD_QUEUE)
    public Queue dlQueue(){
        return new Queue(ORDER_DEAD_QUEUE, true, false, false, null);
    }

    @Bean("ORDER_DEAD_BINDING")
    public Binding deadBinding(){
        return new Binding(ORDER_DEAD_QUEUE, Binding.DestinationType.QUEUE, DEAD_LETTER_EXCHANGE, DEAD_LETTER_QUEUEA_ROUTING_KEY, null);
    }


}
