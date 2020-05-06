package com.oyyo.gmall.wms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @ClassName: SecurityConfig
 * @Description: TODO
 * @Author: LiKui
 * @Date: 2020-4-22 14:21
 * @Version: 1.0
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //放行所有请求
        http.authorizeRequests().antMatchers("/**").permitAll();
        http.csrf().disable();
    }
}