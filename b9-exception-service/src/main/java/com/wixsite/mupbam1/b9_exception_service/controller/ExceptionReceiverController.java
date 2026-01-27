package com.wixsite.mupbam1.b9_exception_service.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wixsite.mupbam1.b9_exception_service.models.ErrorLog;

@RestController
@RequestMapping("/log")
public class ExceptionReceiverController {
    @PostMapping
    public void receiveError(@RequestBody ErrorLog log) {
        // Здесь в будущем будет запись в БД или отправка в Telegram
        System.out.println("RECEIVED ERROR FROM: " + log.serviceName());
        System.err.println("MESSAGE: " + log.errorMessage());
    }
}