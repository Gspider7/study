package com.acrobat.study.security.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysUserRole implements Serializable {

    private Long userId;

    private Long roleId;
}