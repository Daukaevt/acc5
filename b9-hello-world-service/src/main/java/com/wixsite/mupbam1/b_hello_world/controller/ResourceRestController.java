package com.wixsite.mupbam1.b_hello_world.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wixsite.mupbam1.b_hello_world.ResourceService.ResourceService;
import com.wixsite.mupbam1.b_hello_world.entities.Picture;
import com.wixsite.mupbam1.b_hello_world.exceptions.ResourceNotFoundException;

@RestController
@RequestMapping("/hello/api/users")
public class ResourceRestController {

    private final ResourceService resourceService;

    public ResourceRestController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }
    
    // Теперь метод чистый: берем имя только из заголовка X-User-Name
    @GetMapping("/my")
    public List<Picture> getMyPictures(@RequestHeader("X-User-Name") String username) {
        // Логика проста: Гейтвей вытащил имя из JWT и прислал нам.
        // Мы просто идем в базу с этим именем.
        List<Picture> photos = resourceService.findAllByUsername(username);
        
        if (photos.isEmpty()) {
            throw new ResourceNotFoundException("No photos found for user: " + username);
        }
        return photos;
    }

    // Общий список пользователей/фото закрыт (закомментирован)
    /*
    @GetMapping
    public List<Picture> getAllUsers() {
        return resourceService.findAllUsers();
    }
    */

    @GetMapping("/{id}")
    public Picture getUserById(@PathVariable Long id) {
        return resourceService.findUserById(id);
    }
    
    @GetMapping("/test-error")
    public void throwError() {
        throw new RuntimeException("Test Exception for b9-exception-service");
    }
}