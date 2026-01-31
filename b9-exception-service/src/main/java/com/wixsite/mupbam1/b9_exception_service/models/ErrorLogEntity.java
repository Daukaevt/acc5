package com.wixsite.mupbam1.b9_exception_service.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

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
    @Column(columnDefinition = "TEXT") // Чтобы влез длинный текст ошибки
    private String errorMessage;
    @Column(columnDefinition = "TEXT")
    private String stackTrace;
    private Instant timestamp;
}