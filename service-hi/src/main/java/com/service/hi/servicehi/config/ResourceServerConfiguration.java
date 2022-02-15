package com.service.hi.servicehi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * @author:GSHG
 * @date: 2021-12-10 5:44 PM
 * description:
 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
    /**
     * 添加EnableResourceServer注解开启资源服务的功能，加注解EnableGlobalMethodSecurity开户方法级别的保护，
     * ResourceServerConfigurerAdapter是配置类，configure(HttpSecurity http)中只配置了"/order/**"需要验证。
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests().antMatchers("/order/**").authenticated();// 配置order访问控制，必须认证后才可以访问
    }

}
