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

                // 1. Ищем в заголовке
                if (exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        token = authHeader.substring(7);
                    }
                } 
                
                // 2. Ищем в Cookie
                if (token == null && exchange.getRequest().getCookies().containsKey("jwt")) {
                    token = exchange.getRequest().getCookies().getFirst("jwt").getValue();
                }

                // 3. Если токена нет — редирект на логин
                if (token == null) {
                    return redirectToLogin(exchange);
                }

                try {
                    // 4. Валидируем токен
                    Jwts.parserBuilder()
                            .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                            .build()
                            .parseClaimsJws(token);
                    
                } catch (Exception e) {
                    // Если токен "кривой" или просрочен — тоже на логин
                    return redirectToLogin(exchange);
                }
            }
            return chain.filter(exchange);
        };
    }

    private Mono<Void> redirectToLogin(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.SEE_OTHER); // Статус 303 для корректного редиректа браузером
        response.getHeaders().set(HttpHeaders.LOCATION, "/auth/login");
        return response.setComplete();
    }

    // Оставляем старый метод на случай, если он понадобится для других нужд
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        System.out.println("Auth Error: " + err); 
        return response.setComplete();
    }
}