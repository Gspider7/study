package com.acrobat.study.lock.lettuce;

import com.acrobat.study.lock.service.TestService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ValueOperations;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@SpringBootTest
class LettuceLockTests {

    @Autowired
    private TestService testService;
    @Autowired
    private ValueOperations<String, Object> valueOperations;

    static RedisServer redisServer;

    @BeforeAll
    public static void startRedisServer() throws IOException {
//        redisServer = new RedisServer(6779);
        redisServer = RedisServer.builder()
                .port(6779)
                .setting("persistence-available no")
                .setting("maxmemory 209715200")
                .build();
        redisServer.start();
    }

    // 测试的时候记得关闭防火墙...
    @Test
    void test() throws InterruptedException {
        int threadNum = 30;

        CountDownLatch cdl = new CountDownLatch(threadNum);
        for (int i=0; i<threadNum; i++) {
            new Thread(() -> {
                try {
                    cdl.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                testService.testAdd();
            }).start();

            cdl.countDown();
        }

        Thread.sleep(50000L);
        System.out.println(valueOperations.get("testKey"));
    }

    @AfterAll
    public static void shutdownRedisServer() {
        redisServer.stop();
    }
}
