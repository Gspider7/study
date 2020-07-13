package com.acrobat.study.security.service;

import com.acrobat.study.security.entity.SysRole;
import com.acrobat.study.security.entity.SysUser;
import com.acrobat.study.security.entity.SysUserRole;
import com.acrobat.study.security.mapper.SysRoleMapper;
import com.acrobat.study.security.mapper.SysUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * mybatis plus的service实现了一些基本方法
 */
@Service
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> {

    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Transactional
    public void signUp(SysUser sysUser, Set<String> roleSet) {
        save(sysUser);

        long userId = sysUser.getId();              // 保存和批量保存都可以获取自增id

        List<SysUserRole> sysUserRolesToAdd = new ArrayList<>();
        for (String role : roleSet) {
            SysRole sysRole = sysRoleMapper.selectOne(new QueryWrapper<SysRole>().eq("name", role));
            if (sysRole == null) {
                sysRole = new SysRole();
                sysRole.setName(role);
                sysRoleMapper.insert(sysRole);
            }
            sysUserRolesToAdd.add(new SysUserRole(userId, sysRole.getId()));
        }
        if (sysUserRolesToAdd.size() > 0) sysUserRoleService.saveBatch(sysUserRolesToAdd);
    }
}
