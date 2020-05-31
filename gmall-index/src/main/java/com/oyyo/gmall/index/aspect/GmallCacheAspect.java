package com.oyyo.gmall.index.aspect;

import com.alibaba.fastjson.JSON;
import com.oyyo.gmall.index.annotation.GmallCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Component
@Aspect
@Slf4j
public class GmallCacheAspect {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Around("@annotation(com.oyyo.gmall.index.annotation.GmallCache)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{

        log.info("进入缓存查询切面...");
        //目标方法返回值
        Object result = null;
        //获取目标方法参数列表
        Object[] args = joinPoint.getArgs();
        //获取目标方法
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        //获取方法对象
        Method method = signature.getMethod();
        // 获取指定注解
        GmallCache gmallCacheAnnotation = method.getAnnotation(GmallCache.class);
        //获取注解 参数信息
        String prefix = gmallCacheAnnotation.prefix();
        //获取目标方法的返回值
        Class<?> returnType = method.getReturnType();

        //1 从缓存中查询
        String key = prefix + Arrays.asList(args).toString();

        log.info("开始查询缓存：key=[{}],returnType=[{}]",key,returnType);

        result = cacheHit(key, returnType);

        log.info("查询缓存结束，返回结果，result=[{}]",result);
        //2 命中 直接返回
        if (result != null) {
            log.info("缓存命中，返回结果");
            return result;
        }

        log.info("缓存未命中，开始加锁...[{}]","lock" + key);
        //3 没有命中 加分布式锁
        RLock lock = redissonClient.getLock("lock" + key);
        lock.lock();

        //5 再次查询缓存
        log.info("再次查询缓存");
        result = cacheHit(key, returnType);
        //2 命中 直接返回
        if (result != null) {
            //释放分布式锁
            lock.unlock();
            log.info("再次查询缓存命中，返回结果,释放锁");
            return result;
        }
        //6 如果缓存没有 执行目标方法
        log.info("缓存未命中，开始执行目标方法...");
        result = joinPoint.proceed();

        log.info("将目标方法返回结果放入缓存开始...");
        //7 放入缓存
        int timeout = gmallCacheAnnotation.timeout();
        int random = gmallCacheAnnotation.random();
        redisTemplate.opsForValue().set(key,JSON.toJSONString(result), timeout + (int) (Math.random() * random), TimeUnit.MINUTES);
        log.info("将目标方法返回结果放入缓存过期时间...[{}],单位：[{}]",timeout + (int) (Math.random() * random),TimeUnit.MINUTES);
        //8 释放分布式锁
        lock.unlock();
        //9 返回结果
        log.info("释放锁，并返回结果");
        return result;
    }

    /**
     * 查询缓存公共方法
     * @param key
     * @param returnType
     * @return
     */
    private Object cacheHit(String key,Class<?> returnType){
        //1 从缓存中查询
        String resultJson = redisTemplate.opsForValue().get(key);
        //2 命中 直接返回
        if (StringUtils.isNotBlank(resultJson)) {
            return JSON.parseObject(resultJson,returnType);
        }
        return null;
    }
}
