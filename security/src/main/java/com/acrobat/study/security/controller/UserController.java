package com.acrobat.study.security.controller;

import com.acrobat.study.security.entity.SysUser;
import com.acrobat.study.security.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * spring security已经注入了登录/login和登出/logout的rest接口，定义接口时注意地址不要冲突
 * @author xutao
 * @date 2020-07-01 13:50
 */
@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private SysUserService sysUserService;


    @RequestMapping("/login")
    public String showLogin(Model model) {
        return "login";
    }

    @RequestMapping("/home")
    public String showHome(Model model) {
        // 获取当前登录用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String name = authentication.getName();
        log.info("当前登录用户：" + name);

        model.addAttribute("username", name);
        return "home";
    }

    /**
     * hasRole('ROLE_ADMIN')和hasRole('ADMIN')效果一样，都是检查用户是否有名为ROLE_ADMIN的权限
     */
    @RequestMapping("/admin")
    @ResponseBody
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PreAuthorize("hasRole('ADMIN')")
    public String printAdmin() {
        return "如果你看见这句话，说明你有ROLE_ADMIN角色";
    }

    @RequestMapping("/normal")
    @ResponseBody
    @PreAuthorize("hasRole('NORMAL')")
    public String printUser() {
        return "如果你看见这句话，说明你有ROLE_NORMAL角色";
    }

    /**
     * 注册用户测试
     * @param username  用户名
     * @param password  密码
     * @param roles     角色，多个用逗号分割
     * 测试：http://localhost:8101/security/user/signUp?username=t&password=t&roles=ADMIN,temp
     */
    @RequestMapping("/signUp")
    @ResponseBody
    public String signUp(@RequestParam("username") String username,
                         @RequestParam("password") String password,
                         @RequestParam("roles") String roles) {

        SysUser sysUser = new SysUser();
        sysUser.setUsername(username);
        sysUser.setPassword(password);

        Set<String> roleSet = Arrays.stream(roles.split(","))
                .map(String::trim)
                .filter(s -> !StringUtils.isEmpty(s))
                .map(s -> "ROLE_" + s.toUpperCase())
                .collect(Collectors.toSet());

        sysUserService.signUp(sysUser, roleSet);

        return "ok";
    }

}
