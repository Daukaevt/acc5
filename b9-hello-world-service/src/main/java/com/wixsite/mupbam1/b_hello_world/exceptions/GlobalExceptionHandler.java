package com.wixsite.mupbam1.b_hello_world.exceptions;

import com.wixsite.mupbam1.b_hello_world.models.ErrorLog;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClient;

import java.time.Instant; // Добавлен необходимый импорт
import java.util.Arrays;

@RestControllerAdvice
public class GlobalExceptionHandler {
 
    private final RestClient restClient = RestClient.create("http://exception-service:8085");

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorLog> handleAndReport(Exception ex) {
        
        // Формируем отчет, используя Instant.now() для корректной даты
        ErrorLog report = new ErrorLog(
            "hello-service", 
            ex.getMessage(), 
            Arrays.toString(ex.getStackTrace()), // Добавили stackTrace для информативности
            Instant.now() // ТЕПЕРЬ ТУТ ОБЪЕКТ ТИПА INSTANT
        );
        
        try {
            // Отправка в exception-service
            restClient.post()
                      .uri("/log")
                      .body(report)
                      .retrieve()
                      .toBodilessEntity();
            System.out.println("✅ Error report sent to exception-service");
        } catch (Exception e) {
            // Печатаем ошибку отправки, чтобы знать, если сервис упал
            System.err.println("❌ Failed to send log: " + e.getMessage());
        }

        // Возвращаем JSON пользователю
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(report); 
    }
}