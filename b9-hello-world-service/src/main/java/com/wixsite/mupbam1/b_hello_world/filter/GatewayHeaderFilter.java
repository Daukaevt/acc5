package com.wixsite.mupbam1.b_hello_world.filter;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GatewayHeaderFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // Должно быть X-User-Name, как в коде твоего Gateway!
        String username = request.getHeader("X-User-Name"); 

        if (username != null) {
            UsernamePasswordAuthenticationToken auth = 
                new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(auth);
            // Добавь лог, чтобы в консоли видеть, что юзер "пролетел" внутрь
            System.out.println("Extracted username from Gateway: " + username);
        }

        filterChain.doFilter(request, response);
    }
}