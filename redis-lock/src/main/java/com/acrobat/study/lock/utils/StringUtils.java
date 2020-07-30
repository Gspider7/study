package com.acrobat.study.lock.utils;

import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 *
 */
public class StringUtils extends org.springframework.util.StringUtils {

    public static String combine(Collection collection, String combinator) {
        if (CollectionUtils.isEmpty(collection)
                || combinator == null) return null;

        StringBuilder sb = new StringBuilder();
        collection.forEach(item -> {
            sb.append(item.toString()).append(combinator);
        });
        return sb.substring(0, sb.length() - combinator.length());
    }
}
