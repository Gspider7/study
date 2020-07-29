package com.acrobat.study.lock.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisSync {

    /**
     * 锁前缀
     */
    String preKey() default "lock:";

    /**
     * 锁名称
     */
    String key() default "";

    /**
     * 重试次数
     */
    int retryTimes() default 3;

    /**
     * 重试间隔（毫秒）
     */
    long retryInterval() default 1000;

    /**
     * 失效时间
     */
    long expire() default 60000L;

    /**
     * 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

}