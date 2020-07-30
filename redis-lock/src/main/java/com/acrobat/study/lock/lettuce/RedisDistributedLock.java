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

    /* 支持重入 */
    private static final String REENTRANT_LUA = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
            "return redis.call('set', KEYS[1], ARGV[1], 'PX', ARGV[2]) else return nil end";
    private static final String REENTRANT_WITHOUT_EXPIRE_LUA = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
            "return redis.call('set', KEYS[1], ARGV[1]) else return nil end";

    public boolean lock(String key, String value, long expire, TimeUnit timeUnit, int retryTimes, long retryInterval) {
        boolean result = tryLock(key, value, expire, timeUnit);

        // 重试
        while ((!result) && Math.abs(retryTimes) > 0) {
            try {
                log.debug("lock failed, retrying..." + retryTimes);
                Thread.sleep(retryInterval);
            } catch (InterruptedException e) {
                return false;
            }
            result = tryLock(key, value, expire, timeUnit);
            if (retryTimes > 0) retryTimes --;
        }
        return result;
    }

    private boolean tryLock(String key, String value, long expire, TimeUnit timeUnit) {
        boolean result = setIfAbsent(key, value, expire, timeUnit);
        if (!result) result = setIfEqual(key, value, expire, timeUnit);

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

    // 重入方式获得锁
    // eval(script, returnType, numberOfKeys, keys..., args...)
    private boolean setIfEqual(String key, String value, long expire, TimeUnit timeUnit) {
        try {
            RedisCallback<byte[]> callback = (connection) -> {
                long expireMillis = timeUnit.toMillis(expire);
                Charset charset = Charset.forName("UTF-8");

                if (expire < 0) {
                    return connection.eval(REENTRANT_WITHOUT_EXPIRE_LUA.getBytes(), ReturnType.VALUE, 1, key.getBytes(charset),
                            value.getBytes(charset));
                } else {
                    return connection.eval(REENTRANT_LUA.getBytes(), ReturnType.VALUE, 1, key.getBytes(charset),
                            value.getBytes(charset), (expireMillis + "").getBytes(charset));
                }
            };
            byte[] result = redisTemplate.execute(callback);
            return result != null
                    && "OK".equals(new String(result, Charset.forName("UTF-8")));
        } catch (Exception e) {
            log.error("exception occur while releasing redis lock", e);
        }
        return false;
    }

    public boolean unlock(String key, String value) {
        // 释放锁的时候，有可能因为持锁之后方法执行时间大于锁的有效期，此时有可能已经被另外一个线程持有锁，所以不能直接删除
        try {
            RedisCallback<Boolean> callback = (connection) -> {
                Charset charset = Charset.forName("UTF-8");
                return connection.eval(UNLOCK_LUA.getBytes(), ReturnType.BOOLEAN, 1, key.getBytes(charset), value.getBytes(charset));
            };
            return redisTemplate.execute(callback);
        } catch (Exception e) {
            log.error("exception occur while releasing redis lock", e);
        }
        return false;
    }

}
