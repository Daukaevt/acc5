package com.wixsite.mupbam1.b_hello_world.models;

import java.time.LocalDateTime;

public record ErrorLog(
	    String serviceName,
	    String errorMessage,
	    String stackTrace,
	    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	    LocalDateTime timestamp
	) {}