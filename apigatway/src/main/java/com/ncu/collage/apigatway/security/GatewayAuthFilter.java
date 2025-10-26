package com.ncu.collage.apigatway.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class GatewayAuthFilter implements GlobalFilter, Ordered {

    private final WebClient webClient;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Value("${gateway.internal.secret}")
    private String internalSecret;

    public GatewayAuthFilter(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("lb://AUTHSERVICE").build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // Allow auth endpoints and actuator without auth
        if (pathMatcher.match("/auth/**", path) || pathMatcher.match("/actuator/**", path)) {
            return chain.filter(exchange);
        }

        String authorization = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !authorization.startsWith("Basic ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            exchange.getResponse().getHeaders().add(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"Gateway\"");
            return exchange.getResponse().setComplete();
        }

        return webClient.post()
                .uri("/auth/verify")
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .retrieve()
                .toBodilessEntity()
                .flatMap(resp -> {
                    // On success, add internal trust header and forward
                    ServerHttpRequest mutated = exchange.getRequest().mutate()
                            .header("X-Internal-Secret", internalSecret)
                            .build();
                    return chain.filter(exchange.mutate().request(mutated).build());
                })
                .onErrorResume(ex -> {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                });
    }

    @Override
    public int getOrder() {
        return -100; // run early
    }
}
