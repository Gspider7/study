package com.acrobat.study.lock.lettuce;

import com.acrobat.study.lock.service.TestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;

@SpringBootTest
class LettuceLockTests {

    @Autowired
    private TestService testService;

    @Test
    void test() {
        int threadNum = 30;
        int temp = 0;

        CountDownLatch cdl = new CountDownLatch(threadNum);
        for (int i=0; i<threadNum; i++) {
            new Thread(() -> {
                try {
                    cdl.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                testService.testAdd(temp);
            }).start();

            cdl.countDown();
        }

        System.out.println();
    }
}
