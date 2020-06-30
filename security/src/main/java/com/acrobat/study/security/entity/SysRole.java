package com.acrobat.study.security.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("sys_role")
public class SysRole implements Serializable {

    private Long id;

    private String name;
}