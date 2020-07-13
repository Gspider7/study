package com.acrobat.study.oauth2.server.config;

import com.acrobat.study.oauth2.server.service.security.CustomUserDetailsService;
import com.acrobat.study.oauth2.server.utils.JacksonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.annotation.Resource;

/**
 * spring security配置
 * spring security基于filterChain实现认证和鉴权
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Resource(name = "passwordEncoder")
    private PasswordEncoder passwordEncoder;

    /**
     * AuthenticationManager是spring security的核心验证器
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 设置密码编码方式
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    /**
     * 注意，这里配置的所有地址都默认带上了contextPath
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();                                  // 关闭CSRF攻击防御
        http.headers().frameOptions().sameOrigin();             // X-Frame-Options响应头配置，解决h2控制台空白问题

        http.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()     // 跨域预检请求放行
//                .antMatchers("/static/**").permitAll()        // 静态资源允许匿名访问
                .antMatchers("/user/**").permitAll()
                .antMatchers("/test/**").permitAll()
                .antMatchers("/h2/**").hasRole("ADMIN")         // 注意这里配置角色需要去掉ROLE_前缀
                .anyRequest().authenticated();                  // 其他所有请求需要身份认证

        // 登录配置
        http.formLogin() // 表单登录
                // http.httpBasic() // HTTP Basic
                .loginPage("/user/login")                       // 跳转登录页的地址
                .loginProcessingUrl("/login")                   // 登录表单请求的地址，不需要写这个地址的接口，由spring security的过滤器处理
                .successForwardUrl("/user/home");               // 登录成功跳转地址

        // 默认登出请求地址为/logout，会执行如下处理：使session失效，清空SecurityContext，重定向到登录页
//        http.logout()...

        // 权限验证失败处理
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 静态资源等放行
        web.ignoring().antMatchers("/static/**", "/other/**");
    }

    @Bean(name = "passwordEncoder")
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        // 使用SHA-256和随机盐进行编码，每次编码结果都不一样，盐会保存在编码后的结果里
        PasswordEncoder encoder = new BCryptPasswordEncoder();

        String result = encoder.encode("123456");
        System.out.println(result);

        // 用户传入明文密码，与数据库编码后的密码匹配
        boolean matches = encoder.matches("123456", result);
        System.out.println(matches);
    }

    // ----------------------------------------------------------------------

    /**
     * 可通过http.formLogin().successHandler()自定义登录成功的处理
     * 可以进行转发，重定向等
     * 同理可以配置failureHandler
     */
    @Bean(name = "authenticationSuccessHandler")
    public AuthenticationSuccessHandler getAuthenticationSuccessHandler() {
        return (httpServletRequest, httpServletResponse, authentication) -> {
            httpServletResponse.setContentType("application/json;charset=utf-8");
            httpServletResponse.getWriter().write(JacksonUtil.writeValueAsString(authentication));
        };
    }

    /**
     * 处理权限不足时的返回，替换默认的error page
     */
    @Bean(name = "accessDeniedHandler")
    public AccessDeniedHandler getAccessDeniedHandler() {
        return (httpServletRequest, httpServletResponse, e) -> {
            httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            httpServletResponse.setContentType("application/json;charset=utf-8");
            httpServletResponse.getWriter().write("很抱歉，您没有该访问权限");
        };
    }
}