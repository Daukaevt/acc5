package com.wixsite.mupbam1.b9_gateway.filter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Value("${jwt.secret:default_secret_key_placeholder_for_startup}") 
    private String secret;

    public AuthenticationFilter() {
        super(Config.class);
    }

    public static class Config { }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                String token = null;

                // 1. Пытаемся достать токен из заголовка Authorization
                if (exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        token = authHeader.substring(7);
                    }
                } 
                
                // 2. Если в заголовке нет, ищем в Cookie (для нашего редиректа)
                if (token == null && exchange.getRequest().getCookies().containsKey("jwt")) {
                    token = exchange.getRequest().getCookies().getFirst("jwt").getValue();
                }

                // 3. Если токена нет нигде — ошибка
                if (token == null) {
                    return onError(exchange, "Missing Authorization Credentials", HttpStatus.UNAUTHORIZED);
                }

                try {
                    // 4. Валидируем токен
                    Jwts.parserBuilder()
                            .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                            .build()
                            .parseClaimsJws(token);
                    
                } catch (Exception e) {
                    return onError(exchange, "Invalid or Expired Token", HttpStatus.UNAUTHORIZED);
                }
            }
            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        System.out.println("Auth Error: " + err); 
        return response.setComplete();
    }
}