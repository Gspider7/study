package com.acrobat.study.security;

import com.acrobat.study.security.entity.SysUser;
import com.acrobat.study.security.mapper.SysUserMapper;
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

    @Test
    public void test() {
        SysUser user = new SysUser();
        user.setUsername("test");
        user.setPassword("pwd");
        sysUserMapper.insert(user);

        SysUser user1 = sysUserMapper.selectById(1);

        SysUser user2 = sysUserMapper.selectByUsername("test");

        System.out.println();
    }


}
