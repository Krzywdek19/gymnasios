package com.krzywdek19.api_gateway.security;

import com.krzywdek19.api_gateway.config.DevAuthProperties;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final PublicPaths publicPaths;
    private final JwtUtil jwtUtil;
    private final StringRedisTemplate redisTemplate;
    private final DevAuthProperties devAuthProperties;

    private String key(String jti) {
        return "blacklist:jwt:" + jti;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var request = exchange.getRequest();
        var path = request.getURI().getPath();

        log.info("GW request: method={}, path={}", request.getMethod(), path);

        if ("OPTIONS".equalsIgnoreCase(request.getMethod().name())) {
            return chain.filter(exchange);
        }

        if (publicPaths.isPublic(path)) {
            return chain.filter(exchange);
        }

        var authHeader = request.getHeaders().getFirst("Authorization");
        boolean hasAuth = authHeader != null && authHeader.startsWith("Bearer ");

        if (!hasAuth && devAuthProperties.isEnabled()) {
            log.warn("GW dev auth is enabled, but workout-service now requires real Bearer JWT");
        }

        if (!hasAuth) {
            log.warn("GW missing or invalid Authorization header for path {}", path);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        var token = authHeader.substring(7);

        final Claims claims;
        try {
            claims = jwtUtil.parseToken(token);
            log.info("GW token parsed successfully, subject={}", claims.getSubject());
        } catch (Exception e) {
            log.error("GW JWT parse failed for path {}: {}", path, e.getMessage(), e);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String jti = claims.getId();
        if (jti != null) {
            try {
                if (redisTemplate.hasKey(key(jti))) {
                    log.warn("GW token is blacklisted, jti={}", jti);
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }
            } catch (Exception e) {
                log.error("GW Redis blacklist check failed for jti {}: {}", jti, e.getMessage(), e);
            }
        }

        log.info("GW token accepted for subject={}", claims.getSubject());
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}