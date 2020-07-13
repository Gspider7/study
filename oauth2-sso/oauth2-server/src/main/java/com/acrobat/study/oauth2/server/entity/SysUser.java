package com.acrobat.study.oauth2.server.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * mybatis plus的好处在于减少xml sql定义
 *
 * 参考官网：https://mp.baomidou.com/ 中的注解相关文档
 */
@Data
@TableName("sys_user")      // 相当于在xml中自动生成了一个baseResultMap，根据下划线转驼峰的方式进行字段匹配
public class SysUser implements Serializable {

    @TableId(type = IdType.AUTO)                                // 使用数据库自增主键
    @TableField(insertStrategy = FieldStrategy.NOT_NULL)        // 只有在该字段不为null才执行插入，即生成的insert语句带<if test="id != null">条件
    private Long id;

    private String username;

    private String password;

    /** 排除非表字段的映射 */
    @TableField(exist = false)
    private String propertyNotInDB;
}