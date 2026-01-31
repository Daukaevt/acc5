package com.wixsite.mupbam1.b_hello_world.exceptions;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.wixsite.mupbam1.b_hello_world.models.ErrorLog; 

@FeignClient(name = "exception-service")
public interface ExceptionClient {
    
    @PostMapping("/log") 
    void sendError(@RequestBody ErrorLog report);
}