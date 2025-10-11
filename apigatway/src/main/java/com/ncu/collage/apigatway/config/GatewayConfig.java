
package com.ncu.collage.apigatway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route("user-service", r -> r.path("/users/**").uri("lb://USER-SERVICE"))
                .route("post-service", r -> r.path("/posts/**").uri("lb://POST-SERVICE"))
                .build();
    }

}
