package com.wixsite.mupbam1.b_hello_world.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.wixsite.mupbam1.b_hello_world.filter.GatewayHeaderFilter;

@Configuration
@EnableWebSecurity // Используем MVC версию для hello-service
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	/*
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // 1. ВАЖНО: Разрешаем Actuator, чтобы сервис не пропадал из Эврики
                .requestMatchers("/actuator/**").permitAll()
                // 2. Разрешаем твой тест ошибки
                .requestMatchers("/hello/**").permitAll()
                // 3. Остальное под замок
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        return http.build();
        */
    	// В SecurityConfig.java твоего hello-service
    	http
        	.csrf(csrf -> csrf.disable())
        	.sessionManagement(session -> session
        			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        	)
        	.exceptionHandling(ex -> ex
        			.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        	)
        	.addFilterBefore(new GatewayHeaderFilter(), UsernamePasswordAuthenticationFilter.class)
        	.authorizeHttpRequests(auth -> auth
            // 1. Оставляем только служебный вход для Eureka/Healthchecks
        			.requestMatchers("/actuator/**").permitAll()
            // 2. Всё остальное (включая /api/users) — под замок
        			.anyRequest().authenticated()
        );

    	return http.build();
    }    
}