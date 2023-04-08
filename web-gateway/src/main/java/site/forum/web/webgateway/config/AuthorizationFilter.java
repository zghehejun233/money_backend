package site.forum.web.webgateway.config;

import cn.hutool.core.text.AntPathMatcher;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import site.forum.web.common.util.JwtUtil;

@Component
public class AuthorizationFilter implements GlobalFilter, Ordered {
    private final AntPathMatcher matcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String path = request.getURI().getPath();
        if (matcher.match("/student/**", path) || matcher.match("/web-student/**", path)
                || matcher.match("/teacher/**", path) || matcher.match("/web-teacher/**", path)
                || matcher.match("/web-admin/**", path) || matcher.match("/admin/**", path)
                || matcher.match("/auth/email/**", path) || matcher.match("/web-auth/email/**", path)
                || matcher.match("/auth/change", path) || matcher.match("/web-auth/change", path)) {
            String token = request.getHeaders().getFirst("token");

            if (token == null || token.isEmpty()) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }

            try {
                JwtUtil.parseJWT(token);
            } catch (ExpiredJwtException expiredJwtException) {
                expiredJwtException.printStackTrace();
                response.setStatusCode(HttpStatus.FORBIDDEN);
                return response.setComplete();
            } catch (Exception exception) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
