package com.oyyo.gmall.oms.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitmqConfig {


    private static final String ROUTINGKEY = "order.ttl";

    private static final String DEAD_LETTER_EXCHANGE = "order.dead.letter.exchange";
    private static final String DEAD_LETTER_QUEUEA_ROUTING_KEY = "order.dead";
    private static final String DELAY_QUEUEA_NAME = "ORDER-TTL-QUEUE";
    private static final String ORDER_DEAD_QUEUE = "ORDER-DEAD-QUEUE";


    @Bean("ORDER-TTL-QUEUE")
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

    //创建延时交换机
    @Bean
    public Exchange getDelayExchange() {
        return ExchangeBuilder.directExchange(DEAD_LETTER_EXCHANGE).durable(true).build();
    }

    /**
     * 延时队列 绑定 私信交换机
     * @return
     */
    @Bean("ORDER-TTL-BINDING")
    public Binding ttlBinding(){
        return new Binding(DELAY_QUEUEA_NAME, Binding.DestinationType.QUEUE, DEAD_LETTER_EXCHANGE, ROUTINGKEY, null);
    }

    /**
     * 声明一个死信队列
     * @return
     */
    @Bean("ORDER-DEAD-QUEUE")
    public Queue dlQueue(){
        return new Queue(ORDER_DEAD_QUEUE, true, false, false, null);
    }

    /**
     * 绑定死信队列 到死信交换机
     * @return
     */
    @Bean("ORDER-DEAD-BINDING")
    public Binding deadBinding(){
        return new Binding(ORDER_DEAD_QUEUE, Binding.DestinationType.QUEUE, DEAD_LETTER_EXCHANGE, DEAD_LETTER_QUEUEA_ROUTING_KEY, null);
    }


}
