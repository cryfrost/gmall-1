package com.oyyo.gmall.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolConfig {

    /**
     * 线程池维护线程的最少数量
     */
    private static final int CORE_POOL_SIZE = 100;

    /**
     * 线程池维护线程的最大数量
     */
    private static final int MAXIMUM_POOL_SIZE = 500;

    /**
     * 线程池维护线程所允许的空闲时间
     */
    private static final int keep_alive_time = 30;

    /**
     * 线程池的阻塞队列
     */
    private static final int capacity = 10000;

    @Bean
    public ThreadPoolExecutor threadPoolExecutor(){
        /**
         * 当线程池的任务缓存队列已满并且线程池中的线程数目达到maximumPoolSize时，如果还有任务到来就会采取任务拒绝策略，通常有以下四种策略：
         * ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常。 默认--
         * ThreadPoolExecutor.DiscardPolicy：丢弃任务，但是不抛出异常。
         * ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新提交被拒绝的任务
         * ThreadPoolExecutor.CallerRunsPolicy：由调用线程（提交任务的线程）处理该任务
         */

        //线程池的拒接策略 默认为
//        ThreadPoolExecutor.AbortPolicy abortPolicy = new ThreadPoolExecutor.AbortPolicy();
        return new ThreadPoolExecutor(100,500,
                60, TimeUnit.SECONDS,new ArrayBlockingQueue<>(10000), new ThreadPoolExecutor.DiscardPolicy());
    }

}
