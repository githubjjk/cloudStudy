package jjk.csgateway.config;

import com.fasterxml.jackson.core.filter.TokenFilter;
import jjk.csgateway.service.SpringContextService;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: jjk
 * @create: 2020-01-06 14:23
 * @program: cloudStudy
 * @description: 路由配置
 */
@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder){


        return builder.routes()
                .route("Auth",p -> p
                        .path("/auth/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://cs-auth")
                )
                .build();
    }
}
