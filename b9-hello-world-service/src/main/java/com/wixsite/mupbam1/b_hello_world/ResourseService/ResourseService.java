package com.wixsite.mupbam1.b_hello_world.ResourseService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wixsite.mupbam1.b_hello_world.entities.Picture;
import com.wixsite.mupbam1.b_hello_world.repository.ResourseRepository;

@Service
public class ResourseService {

    private final ResourseRepository resourseRepository;

    public ResourseService(ResourseRepository resourseRepository) {
        this.resourseRepository = resourseRepository;
    }

    public List<Picture> findAllUsers() {
        return resourseRepository.findAll();
    }

    public Picture findUserById(Long id) {
        return resourseRepository.findById(id).orElse(null);
    }
}