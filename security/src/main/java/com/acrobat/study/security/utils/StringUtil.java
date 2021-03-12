package com.acrobat.study.security.utils;

import org.springframework.util.StringUtils;

/**
 * @author xutao
 * @date 2021-03-12 15:42
 */
public class StringUtil {

    /**
     * 下划线转驼峰
     */
    public static String underlineToCamel(String str) {
        if (StringUtils.isEmpty(str)) return str;

        StringBuilder sb = new StringBuilder();
        for (int i=0; i<str.length(); i++) {
            char c = str.charAt(i);
            if (isBreakChar(c)) {
                i++;
                for (; i<str.length(); i++) {
                    char next = str.charAt(i);
                    if (isBreakChar(next)) continue;

                    if (next >= 'a' && next <= 'z') {
                        sb.append((char)(next - ((int)'a' - 'A')));
                    }  else {
                        sb.append(next);
                    }
                    break;
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 驼峰转下划线
     */
    public static String camelToUnderline(String str) {
        if (StringUtils.isEmpty(str)) return str;

        StringBuilder sb = new StringBuilder();
        for (int i=0; i<str.length(); i++) {
            char c = str.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                sb.append('_').append((char)(c + ((int)'a' - 'A')));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 是否是中断字符
     */
    public static boolean isBreakChar(char c) {
        return c == '_' || c == '-';
    }
}
