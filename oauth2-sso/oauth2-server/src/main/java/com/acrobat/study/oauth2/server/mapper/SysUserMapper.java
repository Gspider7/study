package com.acrobat.study.oauth2.server.mapper;

import com.acrobat.study.oauth2.server.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 通过继承BaseMapper，提供基础CRUD功能，需要其它sql再定义方法
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    public SysUser selectByUsername(@Param("username") String username);
}