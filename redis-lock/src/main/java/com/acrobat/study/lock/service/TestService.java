package com.acrobat.study.lock.service;

import com.acrobat.study.lock.annotation.RedisLock;
import com.acrobat.study.lock.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author xutao
 * @date 2020-07-29 16:20
 */
@Slf4j
@Service
public class TestService {

    @Autowired
    private ValueOperations<String, Object> valueOperations;

    @RedisLock(key = "testAdd")
    public void testAdd() {
        log.info("start processing: {}", Thread.currentThread().getId());

        String key = "testKey";

        Integer value = (Integer) valueOperations.get(key);
        if (value == null) {
            valueOperations.set(key, 1);
        } else {
            valueOperations.set(key, (value + 1));
        }

        log.info("end processing: {}", Thread.currentThread().getId());
    }

    @RedisLock(key = "user", suffixParamKeys = {"user.id", "user.name"})
//    @RedisLock(key = "user", suffixParamKeys = "user.id")
    public void testSaveUser(User user) {

        System.out.println();
    }

    public static void main(String[] args) {
        System.out.println(TimeUnit.MINUTES.toMillis(3));
    }
}
