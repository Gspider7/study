package com.acrobat.study.lock.lettuce;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * 基于lettuce的redis分布式锁，适合单实例redis
 */
@Slf4j
@Component
public class RedisDistributedLock {

    @Resource(name = "redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    /* 释放锁的lua脚本，保证释放锁是一个原子操作 */
    private static final String UNLOCK_LUA = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
            "return redis.call('del', KEYS[1]) else return 0 end";

    public boolean lock(String key, String value, long expire, TimeUnit timeUnit, int retryTimes, long retryInterval) {
        boolean result = setIfAbsent(key, value, expire, timeUnit);
        // 如果获取锁失败，按照传入的重试次数进行重试
        while ((!result) && retryTimes > 0) {
            try {
                log.debug("lock failed, retrying..." + retryTimes);
                Thread.sleep(retryInterval);
            } catch (InterruptedException e) {
                return false;
            }
            result = setIfAbsent(key, value, expire, timeUnit);
            retryTimes --;
        }
        return result;
    }

    // set nx px
    private boolean setIfAbsent(String key, String value, long expire, TimeUnit timeUnit) {
        try {
            RedisCallback<Boolean> callback = (connection) -> {
                Charset charset = Charset.forName("UTF-8");
                return connection.set(key.getBytes(charset), value.getBytes(charset),
                        Expiration.from(expire, timeUnit), RedisStringCommands.SetOption.SET_IF_ABSENT);
            };
            return redisTemplate.execute(callback);
        } catch (Exception e) {
            log.error("redis lock error.", e);
        }
        return false;
    }

    public boolean unlock(String key, String value) {
        // 释放锁的时候，有可能因为持锁之后方法执行时间大于锁的有效期，此时有可能已经被另外一个线程持有锁，所以不能直接删除
        try {
            RedisCallback<Boolean> callback = (connection) -> {
                return connection.eval(UNLOCK_LUA.getBytes(), ReturnType.BOOLEAN, 1, key.getBytes(Charset.forName("UTF-8")), value.getBytes(Charset.forName("UTF-8")));
            };
            return redisTemplate.execute(callback);
        } catch (Exception e) {
            log.error("exception occur while releasing redis lock", e);
        }
        return false;
    }

}
