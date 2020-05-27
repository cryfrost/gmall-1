package com.oyyo.gmall.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * @ClassName: GmallCorsConfig
 * @Description:  cors 跨域配置
 * @Author: LiKui
 * @Date: 2020-4-24 15:57
 * @Version: 1.0
 */
@Configuration
//@PropertySource("classpath:staticConfig/myStaticConfig.properties")
public class GmallCorsConfig {

    /**
     * 允许跨域的域名
     */
    @Value("${origin.uri}")
//    private String origin ;
    private List<String> origin ;

    @Bean
    public CorsWebFilter corsWebFilter(){
        //跨域配置对象
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        System.out.println("允许跨域域名：。。。" + origin);
//        corsConfiguration.addAllowedOrigin(origin);
        corsConfiguration.setAllowedOrigins(origin);
        //是否允许携带cookie
        corsConfiguration.setAllowCredentials(true);
        //允许所有请求方法
        corsConfiguration.addAllowedMethod("*");
        //允许所有头信息
        corsConfiguration.addAllowedHeader("*");

        //配置源对象
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**",corsConfiguration);
        //返回一个cors 过滤器对象
        return new CorsWebFilter(configurationSource);
    }

}