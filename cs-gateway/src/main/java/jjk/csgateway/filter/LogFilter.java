package jjk.csgateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.lang.annotation.Annotation;
import java.net.URI;

/**
 * @author: jjk
 * @create: 2020-03-06 17:21
 * @program: cloudStudy
 * @description: 日志输出
 */
@Component
@Slf4j
public class LogFilter implements GatewayFilter, Order {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest req = exchange.getRequest();
        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        String uri = "";
        String path = "";
        if (null != route && null != route.getUri()) {
            uri = route.getUri().toString();
        }
        URI uri1 = req.getURI();
        URI ex = UriComponentsBuilder.fromUri(uri1).build(true).toUri();
        log.info("{}--{}--{}", req.getMethod(), ex.toString(), uri);
        return chain.filter(exchange);
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }

    @Override
    public int value() {
        return 0;
    }
}
