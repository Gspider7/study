package com.acrobat.study.security;

import com.acrobat.study.security.entity.SysUser;
import com.acrobat.study.security.mapper.SysUserMapper;
import com.acrobat.study.security.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author xutao
 * @date 2020-07-01 13:31
 */
@SpringBootTest
public class MybatisTest {

    @Autowired
    private SysUserMapper sysUserMapper;
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
        SysUser user3 = sysUserMapper.selectOne(wrapper);

//        sysUserService.saveOrUpdateBatch(..);

        System.out.println();
    }
}
