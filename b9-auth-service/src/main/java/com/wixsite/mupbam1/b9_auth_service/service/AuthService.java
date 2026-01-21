package com.wixsite.mupbam1.b9_auth_service.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.wixsite.mupbam1.b9_auth_service.entities.User;
import com.wixsite.mupbam1.b9_auth_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public String register(String username, String password) { // <--- Меняем void на String
    	if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("User already exists!");
        }
    	User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("ROLE_USER");
        userRepository.save(user);
        
        return "User " + username + " registered successfully!"; // <--- Добавляем возврат текста
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (passwordEncoder.matches(password, user.getPassword())) {
            return jwtService.generateToken(user.getUsername());
        }
        throw new RuntimeException("Invalid credentials");
    }
}
