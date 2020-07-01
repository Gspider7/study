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

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 这里密码没有使用编码，如果需要使用，数据库中存储的密码必须使用同样的方式进行编码才能匹配
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

    /**
     * 注意，这里配置的所有地址都默认带上了contextPath
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 解决h2控制台空白问题，参考：https://www.jianshu.com/p/925d5aece6dc
        http.csrf().disable();                                  // 关闭CSRF攻击防御
        http.headers().frameOptions().sameOrigin();

        http.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()     // 跨域预检请求
//                .antMatchers("/static/**").permitAll()        // 静态资源允许匿名访问
                .antMatchers("/user/**").permitAll()
                .antMatchers("/h2/**").hasRole("ADMIN")         // 注意这里配置角色需要去掉ROLE_前缀
                .anyRequest().authenticated();                  // 其他所有请求需要身份认证

        // 引入内置登录接口，接口地址为/login，注意这里配置的是登录页的地址
        http.formLogin() // 表单登录
                // http.httpBasic() // HTTP Basic
                .loginPage("/user/login")                       // 跳转登录页的地址
                .loginProcessingUrl("/login")                   // 登录表单请求的地址，不需要写这个地址的接口，由spring security处理
                .successForwardUrl("/user/home");               // 登录成功跳转地址

        // 引入内置登出接口
        http.logout().clearAuthentication(true)
                .logoutSuccessUrl(contextPath + "/user/login");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 静态资源等放行
        web.ignoring().antMatchers("/static/**", "/other/**");
    }

    // ----------------------------------------------------------------------
}