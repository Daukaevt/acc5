package com.wixsite.mupbam1.b9_auth_service.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // Секрет из Vault или конфига (минимум 32 символа для HS256)
    @Value("${jwt.secret:default_temporary_secret_key_32_chars_long}")
    private String secret;

    // 24 часа в миллисекундах
    private final long expirationTime = 86400000;
    //private final long expirationTime = 8640;


    // --- ГЕНЕРАЦИЯ ТОКЕНА ---

    public String generateToken(String username) {
        return createToken(new HashMap<>(), username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // --- ПРОВЕРКА И ИЗВЛЕЧЕНИЕ ДАННЫХ ---

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token, String userDetailsName) {
        final String username = extractUsername(token);
        return (username.equals(userDetailsName) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    // --- ВНУТРЕННЯЯ ЛОГИКА ---

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        // Важно: .getBytes() работает для обычных строк. 
        // Если в Vault лежит Base64, используйте Decoders.BASE64.decode(secret)
        byte[] keyBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}