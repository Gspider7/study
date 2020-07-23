package com.acrobat.study.oauth2.server.utils;

import java.security.MessageDigest;

/**
 * MD5加密工具类
 * @date 2020-03-15 12:24
 */
public class MD5Util {

    /**
     * 计算获得MD5
     */
    public static String MD5(String data) {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(data.getBytes("UTF-8"));

            for (byte b : array) {
                sb.append(Integer.toHexString((b & 0xFF) | 0x100));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString().toUpperCase();
    }

    public static void main(String[] args) {
        System.out.println("A"+2);
        System.out.println('A'+2);
        System.out.println((char)('A'+2));
    }

}
