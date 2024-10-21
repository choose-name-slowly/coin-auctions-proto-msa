package com.nameslowly.coinauctions.gateway.config;

import com.nameslowly.coinauctions.gateway.filter.JwtAuthGatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 라우팅시 authFilter 를 거치게 해서 JWT 를 검증하고 유저정보를 라우팅할 서비스에 넘긴다
 */
@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder, JwtAuthGatewayFilter authFilter) {

        return builder.routes()
            // login 시에는 인가(jwt토큰으로 인가) 없이 바로 라우팅
            .route("userauth-service", r -> r.path("/user/login")
                .uri("lb://userauth-service")
            )
            // 회원가입 시에도 인가(jwt토큰으로 인가) 없이 바로 라우팅 - 구체적인 경로로 우선적으로 처리된다
            .route("userauth-service", r -> r.path("/api/auth/sign-up")
                .uri("lb://userauth-service")
            )
            // userAuth
            .route("userauth-service", r -> r.path("/api/auth/**")
                .filters(f -> f.filter(authFilter))
                .uri("lb://userauth-service")
            )
            // auction
            .route("auction-service", r -> r.path("/api/auctions/**")
                .filters(f -> f.filter(authFilter))
                .uri("lb://auction-service")
            )
            // bid
            .route("bidwin-service", r -> r.path("/api/bids/**")
                .filters(f -> f.filter(authFilter))
                .uri("lb://bidwin-service")
            )
            // coin
            .route("coinpay-service", r -> r.path("/api/coins/**", "/api/coin_wallets/**", "/api/coin_histories")
                .filters(f -> f.filter(authFilter))
                .uri("lb://coinpay-service")
            )
            // chat
            .route("chat-service", r -> r.path("/api/chat/**")
                .filters(f -> f.filter(authFilter))
                .uri("lb://chat-service")
            )
            //chat login
            .route("chat-service-login", r -> r.path("/api/chatUser/login-page", "/ws/**", "/chat/**")
                .uri("lb://chat-service")
            )
            // 정적 리소스(js, css, webjars)에 대한 접근
            .route("static-resources", r -> r.path("/js/**", "/css/**", "/webjars/**")
                .uri("lb://chat-service")
            )
            .build();
    }
}