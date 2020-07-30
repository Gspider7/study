package com.acrobat.study.lock.aop;

import com.acrobat.study.lock.annotation.RedisLock;
import com.acrobat.study.lock.lettuce.RedisDistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 基于注解的redis锁切面
 */
@Slf4j
@Component
@Aspect
public class RedisLockAspect {

    @Autowired
    private RedisDistributedLock redisDistributedLock;

    @Pointcut("@annotation(com.acrobat.study.lock.annotation.RedisLock)")
    public void redisSync() {

    }

    @Around("redisSync()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

//        // 获得方法的参数名称 && 值
//        String[] parameterNames = signature.getParameterNames();
//        Object[] args = joinPoint.getArgs();

        RedisLock redisLock = method.getDeclaredAnnotation(RedisLock.class);
        String preKey = redisLock.preKey();
        String lockKey = preKey == null ? redisLock.key() : preKey + redisLock.key();
        Long expire = redisLock.expire();
        TimeUnit timeUnit = redisLock.timeUnit();
        int retryTimes = redisLock.retryTimes();
        long retryInterval = redisLock.retryInterval();

        String lockValue= UUID.randomUUID().toString();     // 这个值可以根据业务定制 必须唯一
        Boolean lockSuccess = redisDistributedLock.lock(lockKey, lockValue, expire, timeUnit, retryTimes, retryInterval);
        if (!lockSuccess) return;
        try {
            joinPoint.proceed();
        } finally {
            redisDistributedLock.unlock(lockKey, lockValue);
        }
    }
}