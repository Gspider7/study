package com.acrobat.study.security.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * @author xutao
 * @date 2020-07-31 15:05
 */
public interface TreeObject extends Serializable {

    @JsonIgnore
    public Object getNodeId();

    @JsonIgnore
    public Object getParentNodeId();

    @JsonIgnore
    public String getText();
}
