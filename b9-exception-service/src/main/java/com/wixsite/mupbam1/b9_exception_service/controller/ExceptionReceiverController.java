package com.wixsite.mupbam1.b9_exception_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wixsite.mupbam1.b9_exception_service.models.ErrorLogEntity;
import com.wixsite.mupbam1.b9_exception_service.repository.ErrorLogRepository;

@RestController
@RequestMapping("/log")
public class ExceptionReceiverController {

    private final ErrorLogRepository repository;

    // Внедряем через конструктор (лучшая практика)
    public ExceptionReceiverController(ErrorLogRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<String> receiveError(@RequestBody ErrorLogEntity log) {
        // 1. Сохраняем в PostgreSQL
        ErrorLogEntity savedLog = repository.save(log);
        
        // 2. Логируем в консоль для отладки
        System.out.println("LOG SAVED TO DB. ID: " + savedLog.getId());
        
        return ResponseEntity.ok("Diagnostic data stored in exception_db");
    }
}