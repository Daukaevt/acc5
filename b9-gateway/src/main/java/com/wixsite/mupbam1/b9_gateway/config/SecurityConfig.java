package com.wixsite.mupbam1.b9_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity // Важно: для Гейтвея используем именно это!
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchanges -> exchanges
                // 1. Разрешаем Эврике и Актуатору (чтобы Гейтвей не пропадал)
                .pathMatchers("/actuator/**").permitAll()
                // 2. Разрешаем авторизацию
                .pathMatchers("/auth/**").permitAll()
                // 3. Разрешаем наш тест ошибки
                .pathMatchers("/hello/api/users/test-error").permitAll()
                // 4. Всё остальное — под замок
                .anyExchange().authenticated()
            )
            .build();
    }
}