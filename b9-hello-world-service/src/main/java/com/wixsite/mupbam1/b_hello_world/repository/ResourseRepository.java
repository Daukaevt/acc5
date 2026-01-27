package com.wixsite.mupbam1.b_hello_world.repository;

import com.wixsite.mupbam1.b_hello_world.entities.Picture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourseRepository extends JpaRepository<Picture, Long> {
}