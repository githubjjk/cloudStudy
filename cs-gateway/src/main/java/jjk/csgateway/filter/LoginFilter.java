package jjk.csgateway.filter;

import jjk.csutils.pojo.ErrorResult;
import jjk.csutils.pojo.PublicState;
import jjk.csutils.service.RedisService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;

/**
 * @author: jjk
 * @create: 2020-02-27 10:31
 * @program: cloudStudy
 * @description: 登录鉴权
 */
@Component
public class LoginFilter implements GatewayFilter {


    @Autowired
    private RedisService redisService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String accessToken = exchange.getRequest().getHeaders().getFirst("AccessToken");
        if (StringUtils.isNotEmpty(accessToken)) {
            String cacheToken = redisService.getVal(accessToken);
            if (StringUtils.isNotEmpty(cacheToken)){
                return chain.filter(exchange);
            }
        }
        return resultNoAuth(exchange);
    }0

    private Mono<Void> resultNoAuth(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        HttpHeaders headers = response.getHeaders();
        //设置返回头
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        headers.add(HttpHeaders.CACHE_CONTROL, "no-store, no-cache, must-revalidate, max-age=0");
        //设置返回状态
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        byte[] resultByte = new byte[0];
        try {
            resultByte = new ErrorResult(PublicState.AUTHFAILURE.getValue(), "需要登录").toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        DataBuffer wrap = response.bufferFactory().wrap(resultByte);
        return response.writeWith(Mono.just(wrap));
    }
}
