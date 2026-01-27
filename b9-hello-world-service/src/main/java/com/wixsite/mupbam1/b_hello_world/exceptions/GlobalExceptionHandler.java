package com.wixsite.mupbam1.b_hello_world.exceptions;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClient;

import com.wixsite.mupbam1.b_hello_world.models.ErrorLog;

@RestControllerAdvice
public class GlobalExceptionHandler {
 
    private final RestClient restClient = RestClient.create();

    @ExceptionHandler(Exception.class)
    // 1. Возвращаем объект ErrorLog, чтобы Spring превратил его в JSON на экране
    public org.springframework.http.ResponseEntity<ErrorLog> handleAndReport(Exception ex) {
        
        ErrorLog report = new ErrorLog(
            "hello-service", 
            ex.getMessage(), 
            null, 
            System.currentTimeMillis()
        );
        
        try {
            // Отправка отчета в exception-service
            restClient.post()
                      .uri("http://exception-service:8085/log")
                      .body(report)
                      .retrieve()
                      .toBodilessEntity();
        } catch (Exception e) {
            // Если сервис диагностики недоступен, пишем в консоль, чтобы не потерять ошибку
            System.err.println("Failed to send log to exception-service: " + e.getMessage());
        }

        // 2. Возвращаем отчет пользователю с кодом 500 (Internal Server Error)
        return org.springframework.http.ResponseEntity
                .status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
                .body(report); 
    }
}