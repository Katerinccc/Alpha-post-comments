package com.posada.santiago.alphapostsandcomments.application.config;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class JwtFilter implements WebFilter {

    public static final String HEADER ="Bearer ";

    private final JwtTokenConfig jwtTokenConfig;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        var token = resolveToken(exchange.getRequest());
        if(StringUtils.hasText(token) && this.jwtTokenConfig.validateToken(token)){
            var authentication = this.jwtTokenConfig.getAuthentication(token);
            return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
        }
        return chain.filter(exchange);
    }

    private String resolveToken(ServerHttpRequest request) {

        var bearerToken=request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        return StringUtils.hasText(bearerToken) && bearerToken.startsWith(HEADER) ?
                bearerToken.substring(7) : null;
    }

}
