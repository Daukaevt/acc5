package com.wixsite.mupbam1.b_hello_world.models;

public record ErrorLog(
		String serviceName,
		String errorMessage,
		String stackTrace,
		long timestamp
		) {}