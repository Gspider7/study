package com.acrobat.study.security;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan("com.acrobat.study.security.mapper")
// 坑！这里开启cglib动态代理，否则无法注入SysUserService，因为动态代理只能代理接口，另外spring.aop.proxy-target-class默认值是true，事务管理的aop机制里默认是false
@EnableTransactionManagement(proxyTargetClass = true)
@SpringBootApplication
public class SecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }

}
