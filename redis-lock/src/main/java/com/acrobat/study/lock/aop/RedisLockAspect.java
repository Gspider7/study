package com.acrobat.study.lock.aop;

import com.acrobat.study.lock.annotation.RedisSync;
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

    @Pointcut("@annotation(com.acrobat.study.lock.annotation.RedisSync)")
    public void redisSync() {

    }

    @Around("redisSync()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

//        // 获得方法的参数名称 && 值
//        String[] parameterNames = signature.getParameterNames();
//        Object[] args = joinPoint.getArgs();

        RedisSync redisSync = method.getDeclaredAnnotation(RedisSync.class);
        String lockKey = redisSync.key();
        Long expire = redisSync.expire();
        TimeUnit timeUnit = redisSync.timeUnit();
        int retryTimes = redisSync.retryTimes();
        long retryInterval = redisSync.retryInterval();

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