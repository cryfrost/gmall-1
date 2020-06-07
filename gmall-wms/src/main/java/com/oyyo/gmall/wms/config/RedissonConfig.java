package com.oyyo.gmall.wms.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Value("${redisson.uri}")
    private String redissonUri;
    @Bean
    public RedissonClient getRedissonClient(){

        Config config = new Config();

        config.useSingleServer().setAddress(redissonUri);
        config.useSingleServer().setRetryAttempts(3);
        config.useSingleServer().setTimeout(10000);
        config.useSingleServer().setConnectionPoolSize(500);
        config.useSingleServer().setRetryInterval(2000);
        RedissonClient redisson = Redisson.create(config);

        return redisson;
    }
}
