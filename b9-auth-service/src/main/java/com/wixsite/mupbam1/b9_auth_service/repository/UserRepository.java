package com.wixsite.mupbam1.b9_auth_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wixsite.mupbam1.b9_auth_service.entities.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Поиск пользователя по имени
    Optional<User> findByUsername(String username);
    
    // Поиск пользователя по email
    Optional<User> findByEmail(String email);
    
    // Проверка, существует ли пользователь с таким email
    Boolean existsByEmail(String email);
    
    // Проверка, существует ли пользователь с таким ником
    Boolean existsByUsername(String username);
}