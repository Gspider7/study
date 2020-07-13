package com.acrobat.study.oauth2.server.utils;

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
}
