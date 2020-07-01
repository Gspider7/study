package com.acrobat.study.security.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * spring security已经注入了登录/login和登出/logout的rest接口，定义接口时注意地址不要冲突
 * @author xutao
 * @date 2020-07-01 13:50
 */
@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {


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

    @RequestMapping("/admin")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String printAdmin() {
        return "如果你看见这句话，说明你有ROLE_ADMIN角色";
    }

    @RequestMapping("/normal")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_NORMAL')")
    public String printUser() {
        return "如果你看见这句话，说明你有ROLE_NORMAL角色";
    }
}
