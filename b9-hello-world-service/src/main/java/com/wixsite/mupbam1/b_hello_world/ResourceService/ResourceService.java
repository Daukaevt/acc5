package com.wixsite.mupbam1.b_hello_world.ResourceService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wixsite.mupbam1.b_hello_world.entities.Picture;
import com.wixsite.mupbam1.b_hello_world.exceptions.ResourceNotFoundException;
import com.wixsite.mupbam1.b_hello_world.repository.ResourceRepository;


@Service
public class ResourceService {

    // 1. Делаем поле final для безопасности
    private final ResourceRepository resourceRepository;

    // 2. Имя конструктора ДОЛЖНО совпадать с именем КЛАССА (ResourсeService)
    // Убираем void — у конструкторов нет типа возврата!
    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public List<Picture> findAllUsers() {
        List<Picture> pictures = resourceRepository.findAll();
        // Метод isEmpty() уже делает проверку, это отлично
        if (pictures.isEmpty()) {
            throw new ResourceNotFoundException("No pictures found in database");
        }
        return pictures;
    }
    
    public List<Picture> findAllByUsername(String username) {
        return resourceRepository.findByUsername(username);
    }

    public Picture findUserById(Long id) {
        return resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Picture with ID " + id + " not found"));
    }
}