package com.wixsite.mupbam1.b9_exception_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wixsite.mupbam1.b9_exception_service.models.ErrorLog;

@RestController
@RequestMapping("/log")
public class ExceptionReceiverController {
	@PostMapping
	public ResponseEntity<String> receiveError(@RequestBody ErrorLog log) {
	    System.out.println("RECEIVED ERROR FROM: " + log.serviceName());
	    System.err.println("MESSAGE: " + log.errorMessage());
	    
	    // Возвращаем статус, чтобы вызывающий сервис знал, что всё ок
	    return ResponseEntity.ok("Diagnostic data captured by Exception Service");
	}
}