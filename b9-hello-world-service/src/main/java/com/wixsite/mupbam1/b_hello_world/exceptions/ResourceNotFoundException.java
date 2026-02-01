package com.wixsite.mupbam1.b_hello_world.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Эта аннотация говорит Spring, что если это исключение не поймано, 
// нужно вернуть статус 404 Not Found
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}