package com.acrobat.study.security.config;

import com.acrobat.study.security.service.security.CustomUserDetailsService;
import com.acrobat.study.security.utils.JacksonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * spring security配置
 * spring security基于filterChain实现认证和鉴权
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)          // 激活@PreAuthorize和@PostAuthorize注解
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    /**
     * AuthenticationManager是spring security的核心验证器
     */
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
        http.csrf().disable();                                  // 关闭CSRF攻击防御
        http.headers().frameOptions().sameOrigin();             // X-Frame-Options响应头配置，解决h2控制台空白问题

        http.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()     // 跨域预检请求放行
                .antMatchers("/user/**").permitAll()
                .antMatchers("/test/**").permitAll()
                .antMatchers("/h2/**").hasRole("ADMIN")         // 注意这里配置角色需要去掉ROLE_前缀
                .anyRequest().authenticated();                  // 其他所有请求需要身份认证

        // 登录配置
        http.formLogin() // 表单登录
                .loginPage("/user/login")                       // 跳转登录页的地址
                .loginProcessingUrl("/login")                   // 登录表单请求的地址，不需要写这个地址的接口，由spring security的过滤器处理
                .successForwardUrl("/user/home");               // 登录成功跳转地址

        // 权限验证失败处理
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 静态资源等放行
        web.ignoring().antMatchers("/static/**", "/other/**");
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