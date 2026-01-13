package com.wixsite.mupbam1.b9_auth_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Отключаем CSRF, так как мы используем JWT ( stateless )
            .csrf(csrf -> csrf.disable())
            
            // 2. Настраиваем доступы
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll() // Разрешаем логин и регистрацию всем
                .anyRequest().authenticated()            // Все остальное только по токену
            )
            
            // 3. Отключаем сессии (теперь за состояние отвечает JWT)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        return http.build();
    }
}