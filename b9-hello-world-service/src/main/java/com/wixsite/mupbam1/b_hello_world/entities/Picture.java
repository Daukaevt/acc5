package com.wixsite.mupbam1.b_hello_world.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "pictures")
@Data
public class Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String description;
    
    // Теперь это поле — главный идентификатор владельца
    private String username; 
    
    private String url;
}