package com.acrobat.study.security.utils;

import java.io.Serializable;

/**
 * @author xutao
 * @date 2020-07-31 15:05
 */
public interface TreeObject extends Serializable {

    public Object getId();

    public Object getParentId();

    public String getText();
}
