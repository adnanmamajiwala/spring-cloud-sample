package com.sample.springcloudgatewaypcf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@SpringBootApplication
@EnableDiscoveryClient
public class SpringCloudGatewayPcfApplication {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("rest_service_route",
                        r -> r.path("/hello")
                                .uri("lb://rest-service"))
                .route("rest_service_route2",
                        r -> r.path("/hello/**")
                                .filters(f -> f.rewritePath("/hello/(?<segment>.*)", "/hello"))
                                .uri("lb://rest-service"))
                .route("rest_service_route2",
                        r -> r.path("/something")
                                .filters(f -> f.rewritePath("/something", "/hello"))
                                .uri("lb://rest-service"))
                .build();
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws Exception {
        return http.httpBasic().and()
                .csrf().disable()
                .authorizeExchange().anyExchange().permitAll()
                .and()
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudGatewayPcfApplication.class, args);
    }
}
