package com.oyyo.gmall.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

/**
 * gateway 工厂
 */
@Component
public class AuthGatewayFilterFactory extends AbstractGatewayFilterFactory {


    @Autowired
    private AuthGatewayFilter authGatewayFilter;

    @Override
    public GatewayFilter apply(Object config) {

        return authGatewayFilter;
    }
}
