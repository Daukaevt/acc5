package com.wixsite.mupbam1.b_hello_world.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wixsite.mupbam1.b_hello_world.ResourceService.ResourceService;
import com.wixsite.mupbam1.b_hello_world.entities.Picture;
import com.wixsite.mupbam1.b_hello_world.exceptions.ResourceNotFoundException; // Импортируем наш новый класс

@RestController
@RequestMapping("/hello/api/users")
public class ResourceRestController {

    private final ResourceService resourceService;

    public ResourceRestController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping
    public List<Picture> getAllUsers() {
        // Сервис сам выкинет ResourceNotFoundException, если список пуст
        return resourceService.findAllUsers();
    }

    @GetMapping("/{id}")
    public Picture getUserById(@PathVariable Long id) {
        // Сервис сам выкинет ResourceNotFoundException, если ID не найден
        return resourceService.findUserById(id);
    }
    
    @GetMapping("/test-error")
    public void throwError() {
        throw new RuntimeException("Test Exception for b9-exception-service");
    }
}