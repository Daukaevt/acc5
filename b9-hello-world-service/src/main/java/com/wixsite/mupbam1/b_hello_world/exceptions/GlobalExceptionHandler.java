package com.wixsite.mupbam1.b_hello_world.exceptions;

import com.wixsite.mupbam1.b_hello_world.models.ErrorLog;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClient;

@RestControllerAdvice
public class GlobalExceptionHandler {
 
    // Создаем клиент один раз. 
    // ВАЖНО: имя хоста 'exception-service' должно совпадать с именем контейнера в Docker
    private final RestClient restClient = RestClient.create("http://exception-service:8085");

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorLog> handleAndReport(Exception ex) {
        
        // Формируем отчет
        ErrorLog report = new ErrorLog(
            "hello-service", 
            ex.getMessage(), 
            null, // Можно добавить Arrays.toString(ex.getStackTrace()) если нужно
            System.currentTimeMillis()
        );
        
        // 1. Пытаемся отправить отчет "тихо" (в фоне)
        try {
            restClient.post()
                      .uri("/log")
                      .body(report)
                      .retrieve()
                      .toBodilessEntity();
            System.out.println("✅ Error report sent to exception-service");
        } catch (Exception e) {
            System.err.println("❌ Failed to send log: " + e.getMessage());
        }

        // 2. Возвращаем ответ пользователю
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(report); 
    }
}