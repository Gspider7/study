package com.acrobat.study.oauth2.server.service;

import com.acrobat.study.oauth2.server.entity.SysUser;
import com.acrobat.study.oauth2.server.mapper.SysUserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * mybatis plus的service实现了一些基本方法
 */
@Service
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> {

}
