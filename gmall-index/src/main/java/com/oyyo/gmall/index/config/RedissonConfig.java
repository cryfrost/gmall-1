package com.oyyo.gmall.index.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    public RedissonClient getRedissonClient(){

        Config config = new Config();
        config.useSingleServer().setAddress("myredisserver:6379");
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}
