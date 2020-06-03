package com.oyyo.gmall.gateway.config;

import com.oyyo.core.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * gate过滤器
 */
@Component
@EnableConfigurationProperties(JwtProperties.class)
@Slf4j
public class AuthGatewayFilter implements GatewayFilter {

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        //获取jwt类型的token
        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        if (CollectionUtils.isEmpty(cookies)) {
            //身份未验证状态码
            log.info("获取cookies为空,被拦截");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        HttpCookie cookie = cookies.getFirst(jwtProperties.getCookieName());
        if (cookie == null) {
            //身份未验证状态码
            log.info("获取cookie为空，被拦截");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        //解析jwt token
        try {
            JwtUtils.getInfoFromToken(cookie.getValue(), jwtProperties.getPublicKey());
        } catch (Exception e) {
            log.error("解析token异常，被拦截",e);
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();

        }

        //正常解析放行 否则拦截
        log.info("正常放行");
        return chain.filter(exchange);
    }

}
