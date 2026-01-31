package com.wixsite.mupbam1.b_hello_world.models;

import java.time.Instant;

public record ErrorLog(
		String serviceName,
		String errorMessage,
		String stackTrace,
		Instant timestamp
		) {}