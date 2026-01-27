package com.wixsite.mupbam1.b9_auth_service.models;

public record ErrorLog(
		String serviceName,
		String errorMessage,
		String stackTrace,
		long timestamp
		) {}
