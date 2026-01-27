package com.wixsite.mupbam1.b_hello_world.exceptions;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClient;

import com.wixsite.mupbam1.b_hello_world.models.ErrorLog;


@RestControllerAdvice
public class GlobalExceptionHandler {
 
 private final RestClient restClient = RestClient.create();

 @ExceptionHandler(Exception.class)
 public void handleAndReport(Exception ex) {
     ErrorLog report = new ErrorLog("hello-service", ex.getMessage(), null, System.currentTimeMillis());
     
     // Отправляем отчет в наш новый микросервис через Gateway или напрямую по имени через LoadBalancer
     restClient.post()
     		   .uri("http://exception-service:8085/log")
               .body(report)
               .retrieve()
               .toBodilessEntity();
 }
}