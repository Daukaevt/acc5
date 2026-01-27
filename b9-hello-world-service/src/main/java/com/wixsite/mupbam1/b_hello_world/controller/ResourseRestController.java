package com.wixsite.mupbam1.b_hello_world.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wixsite.mupbam1.b_hello_world.ResourseService.ResourseService;
import com.wixsite.mupbam1.b_hello_world.entities.Picture;

@RestController
@RequestMapping("/hello/api/users") // Общий путь для API
public class ResourseRestController {

    private final ResourseService resourseService;

    public ResourseRestController(ResourseService resourseService) {
        this.resourseService = resourseService;
    }

    // 1. Возвращаем список всех пользователей в JSON
    @GetMapping
    public List<Picture> getAllUsers() {
        return resourseService.findAllUsers(); 
    }

    // 2. Возвращаем конкретного пользователя по ID в JSON
    @GetMapping("/{id}")
    public Picture getUserById(@PathVariable Long id) {
        return resourseService.findUserById(id);
    }
}