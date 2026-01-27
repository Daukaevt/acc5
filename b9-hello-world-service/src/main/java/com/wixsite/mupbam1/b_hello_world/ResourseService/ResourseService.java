package com.wixsite.mupbam1.b_hello_world.ResourseService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wixsite.mupbam1.b_hello_world.entities.Picture;
import com.wixsite.mupbam1.b_hello_world.repository.ResourseRepository;

@Service
public class ResourseService {

    // 1. Добавь final, чтобы гарантировать инициализацию
    private final ResourseRepository resourseRepository;

    // 2. Создай конструктор! Spring сам передаст сюда репозиторий
    public ResourseService(ResourseRepository resourseRepository) {
        this.resourseRepository = resourseRepository;
    }

    public List<Picture> findAllUsers() {
        // Теперь здесь не будет NullPointerException
        return resourseRepository.findAll();
    }

    public Picture findUserById(Long id) {
        return resourseRepository.findById(id).orElse(null);
    }
}