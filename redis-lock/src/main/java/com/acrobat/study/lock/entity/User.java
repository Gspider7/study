package com.acrobat.study.lock.entity;

import lombok.Data;

/**
 * @author xutao
 * @date 2020-07-30 16:04
 */
@Data
public class User {

    private Long id;

    private String name;

    public User() {
    }

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
