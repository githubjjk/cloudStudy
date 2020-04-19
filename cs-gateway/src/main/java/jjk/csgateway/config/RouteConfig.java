package jjk.csgateway.config;

import jjk.csgateway.constants.PublicState;
import jjk.csgateway.filter.LogFilter;
import jjk.csgateway.filter.LoginFilter;
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
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        LoginFilter loginFilter = SpringContextService.getBean("loginFilter");
        LogFilter logFilter = SpringContextService.getBean("logFilter");

        return builder.routes()
                .route("Auth", p -> p
                        .path("/api/auth/**")
                        .filters(f -> f
                                .filter(logFilter)
                                .stripPrefix(2))
                        .uri(PublicState.AUTH_MOUDLE)
                )
                .route("AuthBus", p -> p
                        .path("/api/authFun/**")
                        .filters(f -> f
                                .stripPrefix(2)
                                .filter(logFilter)
                                .filter(loginFilter))
                        .uri(PublicState.AUTH_MOUDLE)
                )
                .build();
    }
}
