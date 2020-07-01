package com.acrobat.study.security.mapper;

import com.acrobat.study.security.entity.SysUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    public List<SysUserRole> selectByUserId(@Param("userId") Long userId);
}