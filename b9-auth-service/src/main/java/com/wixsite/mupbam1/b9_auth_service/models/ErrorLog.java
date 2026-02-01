package com.wixsite.mupbam1.b9_auth_service.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record ErrorLog(
    String serviceName,
    String errorMessage,
    String stackTrace,
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime timestamp
) {}