package com.acrobat.study.mybatisplus3;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Properties;

@SpringBootTest
class Mybatisplus3ApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testEnvironment() {
        Properties properties = System.getProperties();

        System.out.println(properties.get("user.dir"));
    }
}
