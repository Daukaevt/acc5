package com.wixsite.mupbam1.b9_exception_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.wixsite.mupbam1.b9_exception_service.models.ErrorLogEntity;
import com.wixsite.mupbam1.b9_exception_service.repository.ErrorLogRepository;
import java.util.List;

@RestController
@RequestMapping("/exceptions/log")
public class ExceptionReceiverController {

    private final ErrorLogRepository repository;

    public ExceptionReceiverController(ErrorLogRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<String> receiveError(@RequestBody ErrorLogEntity log) {
        System.out.println("📩 Попытка сохранения лога от: " + log.getServiceName());
        try {
            ErrorLogEntity savedLog = repository.save(log);
            return ResponseEntity.ok("Diagnostic data stored with ID: " + savedLog.getId());
        } catch (Exception e) {
            System.err.println("❌ Ошибка при записи в БД: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // Получить ВСЕ логи (сначала новые)
    @GetMapping
    public List<ErrorLogEntity> getAllLogs() {
        return repository.findAllByOrderByTimestampDesc();
    }

    // Получить логи конкретного сервиса
    // Пример: GET /log/service/hello-service
    @GetMapping("/service/{name}")
    public List<ErrorLogEntity> getLogsByService(@PathVariable String name) {
        return repository.findByServiceNameOrderByTimestampDesc(name);
    }

    // Очистить все логи (удобно для админки)
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearAllLogs() {
        repository.deleteAll();
        return ResponseEntity.noContent().build();
    }
}