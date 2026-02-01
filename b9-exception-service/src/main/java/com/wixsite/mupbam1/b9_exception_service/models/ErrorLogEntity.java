package com.wixsite.mupbam1.b9_exception_service.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exception_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String serviceName;
    @Column(columnDefinition = "TEXT")
    private String errorMessage;
    @Column(columnDefinition = "TEXT")
    private String stackTrace;

    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private java.time.LocalDateTime timestamp; 
}