package com.sample.springcloudgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class SpringCloudGatewayApplication {

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

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudGatewayApplication.class, args);
    }
}
