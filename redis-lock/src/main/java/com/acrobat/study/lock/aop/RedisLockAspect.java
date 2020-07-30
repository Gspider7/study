package com.acrobat.study.lock.aop;

import com.acrobat.study.lock.annotation.RedisLock;
import com.acrobat.study.lock.lettuce.RedisDistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.acrobat.study.lock.utils.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
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
    public void redisLock() {

    }

    @Around("redisLock()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        RedisLock redisLock = method.getDeclaredAnnotation(RedisLock.class);
        Long expire = redisLock.expire();
        TimeUnit timeUnit = redisLock.timeUnit();
        int retryTimes = redisLock.retryTimes();
        long retryInterval = redisLock.retryInterval();
        // 拼接得到最终的key
        String lockKey = getLockKey(joinPoint, redisLock);

        String lockValue= UUID.randomUUID().toString();     // 这个值可以根据业务定制，但必须唯一
        Boolean lockSuccess = redisDistributedLock.lock(lockKey, lockValue, expire, timeUnit, retryTimes, retryInterval);
        if (!lockSuccess) return;
        try {
            joinPoint.proceed();
        } finally {
            redisDistributedLock.unlock(lockKey, lockValue);
        }
    }

    /**
     * 获得锁的key
     * 由3部分组成：前缀 + 关键字 + 后缀
     * 后缀来自于方法的参数，或者参数内部的属性
     */
    private String getLockKey(JoinPoint joinPoint, RedisLock redisLock) throws NoSuchFieldException {
        List<String> keyPartList = new ArrayList<>();           // 组成key的各个部分
        String preKey = redisLock.preKey();
        if (!StringUtils.isEmpty(preKey)) keyPartList.add(preKey);
        String key = redisLock.key();
        if (!StringUtils.isEmpty(key)) keyPartList.add(key);

        String[] suffixParamKeys = redisLock.suffixParamKeys();
        if (suffixParamKeys != null && suffixParamKeys.length > 0) {
            /* key：参数名，value：参数值 */
            Map<String, Object> argsMap = new HashMap<>();
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String[] parameterNames = signature.getParameterNames();
            Object[] args = joinPoint.getArgs();
            for (int i=0; i<parameterNames.length; i++) {
                argsMap.put(parameterNames[i], args[i]);
            }

            for (String path : suffixParamKeys) {
                if (path == null) continue;

                String property = getPropertyByPath(argsMap, path);
                if (property != null) keyPartList.add(property);
            }
        }

        return StringUtils.combine(keyPartList, ":");
    }

    /**
     * 根据参数上下文和提供的路径，找到参数的值
     */
    private String getPropertyByPath(Map<String, Object> context, String path) {
        // 路径用.连接
        String[] terms = path.split("\\.");

        Object arg = context.get(terms[0]);
        try {
            for (int i = 1; i < terms.length; i++) {
                if (arg == null) return null;

                Field field = arg.getClass().getDeclaredField(terms[i]);
                field.setAccessible(true);
                arg = field.get(arg);               // 递归获取子属性
            }
        } catch (ReflectiveOperationException e) {
            return null;
        }
        return arg == null ? null : arg.toString();
    }
}