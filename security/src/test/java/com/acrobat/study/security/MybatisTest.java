package com.acrobat.study.security;

import com.acrobat.study.security.entity.SysUser;
import com.acrobat.study.security.entity.SysUserRole;
import com.acrobat.study.security.mapper.SysUserMapper;
import com.acrobat.study.security.mapper.SysUserRoleMapper;
import com.acrobat.study.security.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author xutao
 * @date 2020-07-01 13:31
 */
@SpringBootTest
public class MybatisTest {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysUserService sysUserService;


    @Test
    public void test() {
        SysUser user = new SysUser();
        user.setUsername("test");
        user.setPassword("pwd");
        sysUserMapper.insert(user);

        SysUser user1 = sysUserMapper.selectById(1);

        SysUser user2 = sysUserMapper.selectByUsername("test");

        // QueryWrapper用来组装where条件
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username", "test").select("username", "password");
//        wrapper.eq("username", null).select("username", "password");
        SysUser user3 = sysUserMapper.selectOne(wrapper);

        List<SysUserRole> userRoleList = sysUserRoleMapper.selectList(new QueryWrapper<SysUserRole>().eq("user_id", 1));

        // 测试分页插件
        Page<SysUser> page = new Page<>(1, 20);
        sysUserService.page(page, new QueryWrapper<>());

//        sysUserService.saveOrUpdateBatch(..);

        System.out.println();
    }
}
