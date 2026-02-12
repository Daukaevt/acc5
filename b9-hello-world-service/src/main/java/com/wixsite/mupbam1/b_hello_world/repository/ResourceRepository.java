package com.wixsite.mupbam1.b_hello_world.repository;

import com.wixsite.mupbam1.b_hello_world.entities.Picture;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Picture, Long> {
    // Spring сам создаст запрос: SELECT * FROM pictures WHERE username = ?
    List<Picture> findByUsername(String username);
}