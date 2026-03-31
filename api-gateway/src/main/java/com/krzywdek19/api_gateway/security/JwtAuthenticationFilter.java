package com.krzywdek19.api_gateway.security;

import com.krzywdek19.api_gateway.config.DevAuthProperties;
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
            log.info("GW skipping OPTIONS request");
            return chain.filter(exchange);
        }

        if (publicPaths.isPublic(path)) {
            log.info("GW public path: {}", path);
            return chain.filter(exchange);
        }

        var authHeader = request.getHeaders().getFirst("Authorization");
        boolean hasAuth = authHeader != null && authHeader.startsWith("Bearer ");

        log.info("GW auth header present: {}", hasAuth);

        if (!hasAuth && devAuthProperties.isEnabled()) {
            String email = devAuthProperties.getEmail();

            var mutatedRequest = request.mutate()
                    .header("X-User-Email", email)
                    .build();

            log.info("GW dev auth enabled, forwarding request with X-User-Email={}", email);

            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        }

        if (!hasAuth) {
            log.warn("GW missing or invalid Authorization header for path {}", path);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        var token = authHeader.substring(7);

        try {
            var claims = jwtUtil.parseToken(token);
            log.info("GW token parsed successfully, subject={}", claims.getSubject());

            String jti = claims.getId();
            if (jti != null && Boolean.TRUE.equals(redisTemplate.hasKey(key(jti)))) {
                log.warn("GW token is blacklisted, jti={}", jti);
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }

            var mutatedRequest = request.mutate()
                    .header("X-User-Email", claims.getSubject())
                    .build();

            log.info("GW forwarding request with X-User-Email={}", claims.getSubject());

            return chain.filter(exchange.mutate().request(mutatedRequest).build());

        } catch (Exception e) {
            log.error("GW JWT parse failed for path {}: {}", path, e.getMessage(), e);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }
}