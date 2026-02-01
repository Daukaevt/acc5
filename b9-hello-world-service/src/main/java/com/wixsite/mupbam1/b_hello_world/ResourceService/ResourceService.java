package com.wixsite.mupbam1.b_hello_world.ResourceService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wixsite.mupbam1.b_hello_world.entities.Picture;
import com.wixsite.mupbam1.b_hello_world.exceptions.ResourceNotFoundException; // Наш новый класс
import com.wixsite.mupbam1.b_hello_world.repository.ResourceRepository;

@Service
public class ResourсeService {

    // 1. Делаем поле final для безопасности
    private final ResourceRepository resourсeRepository;

    // 2. Имя конструктора ДОЛЖНО совпадать с именем КЛАССА (ResourсeService)
    // Убираем void — у конструкторов нет типа возврата!
    public ResourсeService(ResourceRepository resourсeRepository) {
        this.resourсeRepository = resourсeRepository;
    }

    public List<Picture> findAllUsers() {
        List<Picture> pictures = resourсeRepository.findAll();
        // Метод isEmpty() уже делает проверку, это отлично
        if (pictures.isEmpty()) {
            throw new ResourceNotFoundException("No pictures found in database");
        }
        return pictures;
    }

    public Picture findUserById(Long id) {
        return resourсeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Picture with ID " + id + " not found"));
    }
}