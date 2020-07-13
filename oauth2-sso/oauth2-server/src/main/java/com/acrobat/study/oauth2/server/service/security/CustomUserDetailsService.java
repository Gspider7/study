package com.acrobat.study.oauth2.server.service.security;

import com.acrobat.study.oauth2.server.entity.SysRole;
import com.acrobat.study.oauth2.server.entity.SysUser;
import com.acrobat.study.oauth2.server.entity.SysUserRole;
import com.acrobat.study.oauth2.server.mapper.SysRoleMapper;
import com.acrobat.study.oauth2.server.mapper.SysUserMapper;
import com.acrobat.study.oauth2.server.mapper.SysUserRoleMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * spring security通过容器中的UserDetailsService访问用户信息
 */
@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        // 从数据库中取出用户信息
        SysUser user = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("username", username));

        // 判断用户是否存在
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }

        // 获取用户权限，权限包括角色和权限两部分，在security中角色和权限是平级的，没有关联关系
        List<SysUserRole> userRoles = sysUserRoleMapper.selectList(new QueryWrapper<SysUserRole>()
                .eq("user_id", user.getId()));
        for (SysUserRole userRole : userRoles) {
            SysRole role = sysRoleMapper.selectById(userRole.getRoleId());
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        // 返回UserDetails实现类
        return new User(username, user.getPassword(), authorities);
    }
}