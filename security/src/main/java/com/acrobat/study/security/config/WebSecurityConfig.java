package com.acrobat.study.security.config;

import com.acrobat.study.security.service.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * spring security配置
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Value("server.servlet.context-path")
    private String contextPath;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 这里密码没有使用编码
        auth.userDetailsService(userDetailsService).passwordEncoder(new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return charSequence.toString();
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return s.equals(charSequence.toString());
            }
        });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();                     // 关闭CSRF跨域

        http.authorizeRequests()
                // 跨域预检请求
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                .antMatchers("/static/**").permitAll()        // 静态资源允许匿名访问
                .antMatchers("/user/**").permitAll()
                .anyRequest().authenticated();                  // 其他所有请求需要身份认证

        // 引入内置登录接口，接口地址为/login，注意这里配置的是登录页的地址
        http.formLogin().loginPage(contextPath + "/user/login")
                .defaultSuccessUrl(contextPath + "/user/home")
                .failureForwardUrl(contextPath + "/user/login");
                // 自定义登录用户名和密码参数，默认为username和password
//                .usernameParameter("username")
//                .passwordParameter("password")

        // 引入内置登出接口
        http.logout().clearAuthentication(true)
                .logoutSuccessUrl(contextPath + "/user/login");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 静态资源等放行
        web.ignoring().antMatchers("/static/**", "/other/**");
    }
}