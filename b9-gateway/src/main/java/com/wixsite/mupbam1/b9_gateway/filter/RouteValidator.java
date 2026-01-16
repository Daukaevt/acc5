package com.wixsite.mupbam1.b9_gateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {
	public static final List<String> openApiEndpoints = List.of(
	        "/auth/register",
	        "/auth/login",
	        "/auth/token",
	        "/eureka"
	);

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}