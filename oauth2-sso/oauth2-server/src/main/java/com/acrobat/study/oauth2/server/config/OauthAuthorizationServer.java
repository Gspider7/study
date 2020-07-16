package com.acrobat.study.oauth2.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

/**
 * Oauth2认证服务器配置
 *      AuthorizationServerConfigurerAdapter是AuthorizationServerConfigurer的空实现，即默认配置
 * 参考：
 *      https://projects.spring.io/spring-security-oauth/docs/oauth2.html，开发者文档
 *      https://www.springframework.org/schema/security/spring-security-oauth2.xsd，命名空间文件，可以查阅一些配置项的说明
 *
 * Oauth2的核心认证过滤器是OAuth2AuthenticationProcessingFilter，默认的认证管理器是OAuth2AuthenticationManager
 */
@Configuration
@EnableAuthorizationServer      // 开启oauth认证服务器支持
public class OauthAuthorizationServer extends AuthorizationServerConfigurerAdapter {


    /**
     * @param authorizationServerSecurityConfigurer
     *      继承自SecurityConfigurerAdapter，也可以配置HttpSecurity（与spring security配置的是同一个，包括密码编码器等都是共用的）
     *      生产环境建议开启sslOnly，防止token被盗用
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer authorizationServerSecurityConfigurer) throws Exception {

    }

    /**
     * @param clientDetailsServiceConfigurer
     *      配置一个或多个客户端，这里的配置会覆盖配置文件中的配置
     *      客户端的信息存储在ClientDetails的实现类中，包括id，密码，支持的授权方式，权限范围，授权码时效等
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clientDetailsServiceConfigurer) throws Exception {

    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer authorizationServerEndpointsConfigurer) throws Exception {

    }
}
