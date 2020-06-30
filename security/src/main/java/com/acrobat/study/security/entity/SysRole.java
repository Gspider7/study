package com.acrobat.study.security.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("sys_role")
public class SysRole implements Serializable {

    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
    private Long id;

    private String name;
}