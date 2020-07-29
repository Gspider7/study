package com.acrobat.study.lock.service;

import com.acrobat.study.lock.annotation.RedisSync;
import org.springframework.stereotype.Service;

/**
 * @author xutao
 * @date 2020-07-29 16:20
 */
@Service
public class TestService {

    @RedisSync(key = "testAdd")
    public void testAdd(int i) {
        i ++;
        System.out.println(i);
    }
}
