package com.acrobat.study.lock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import redis.embedded.RedisServer;

import java.io.IOException;


/**
 * 基于lettuce客户端的redis分布式锁，单机版
 * 集群版待追加（基于Redisson客户端）
 */
@SpringBootApplication
public class RedisLockApplication {

    public static void main(String[] args) throws IOException {
        // 启动嵌入式redis，参考：https://github.com/kstyrc/embedded-redis
        RedisServer redisServer = new RedisServer(6779);
        redisServer.start();

        SpringApplication.run(RedisLockApplication.class, args);
    }

}
