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
        // windows下不配置任何参数启动会报堆内存不足，在报错的地方断点可看到如下信息
        // The Windows version of Redis allocates a memory mapped heap for sharing with
        // the forked process used for persistence operations. In order to share this
        // memory, Windows allocates from the system paging file a portion equal to the
        // size of the Redis heap. At this time there is insufficient contiguous free
        // space available in the system paging file for this operation (Windows error
        // 0x5AF). To work around this you may either increase the size of the system
        // paging file, or decrease the size of the Redis heap with the --maxheap flag.
        // Sometimes a reboot will defragment the system paging file sufficiently for
        // this operation to complete successfully.
        RedisServer redisServer = RedisServer.builder()
                .port(6779)
                .setting("persistence-available no")                // 关闭持久化
                .setting("maxmemory 209715200")                     // 限制最大使用内存200M
                .build();
        redisServer.start();

        SpringApplication.run(RedisLockApplication.class, args);
    }

}
