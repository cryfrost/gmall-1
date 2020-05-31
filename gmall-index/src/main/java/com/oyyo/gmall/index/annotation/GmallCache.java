package com.oyyo.gmall.index.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GmallCache {

    /**
     * 缓存前缀
     * @return
     */
    @AliasFor("prefix")
    String value() default "";
    @AliasFor("value")
    String prefix() default "";

    /**
     * 缓存过期时间 ： s
     * @return
     */
    int timeout() default 300;

    /**
     * 防止缓存雪崩指定的随机值范围
     * @return
     */
    int random() default 300;
}
