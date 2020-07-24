package com.acrobat.study.security.utils;

import java.util.UUID;

/**
 * UUID生成工具类
 */
public class UUIDUtil {

    /**
     * 生成唯一UUID
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static void main(String[] args) {

        System.out.println(String.valueOf(null));
    }
}
