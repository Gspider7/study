package com.acrobat.study.security.utils.sql;

import com.acrobat.study.security.utils.StringUtil;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;

/**
 * sql操作工具类
 * @author xutao
 * @date 2021-03-12 15:39
 */
public class SqlUtil {

    /**
     * 根据bean生成建表语句，基础版，可自定义注解实现复杂功能
     */
    public static String generateCreate(Class c) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ");

        String className = c.getSimpleName();
        String tableName = StringUtil.camelToUnderline(className).toUpperCase();
        sb.append(tableName).append(" (").append("\n");

        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            String columnDesc = generateColumnDesc(field);
            sb.append(columnDesc).append("\n");
        }

        sb.setLength(sb.length() - 2);
        sb.append("\n").append(");").append("\n");
        return sb.toString();
    }

    private static String generateColumnDesc(Field field) {
        StringBuilder sb = new StringBuilder();

        String columnName = StringUtil.camelToUnderline(field.getName()).toUpperCase();
        sb.append(columnName).append(" ");

        Class type = field.getType();
        if (type == String.class) {
            sb.append("VARCHAR(32)").append(" ");
        }
        else if (type == int.class || type == Integer.class) {
            sb.append("INTEGER").append(" ");
        }
        else if (type == short.class || type == Short.class) {
            sb.append("SMALLINT").append(" ");
        }
        else if (type == boolean.class || type == Boolean.class) {
            sb.append("TINYINT").append(" ");
        }
        else if (type == float.class || type == Float.class) {
            sb.append("DECIMAL(10, 2)").append(" ");
        }
        else if (type == double.class || type == Double.class) {
            sb.append("DECIMAL(10, 2)").append(" ");
        }
        else if (type == BigDecimal.class) {
            sb.append("DECIMAL(10, 2)").append(" ");
        }
        else if (type == Date.class) {
            sb.append("DATETIME").append(" ");
        }

        sb.append(",");
        return sb.toString();
    }
}
