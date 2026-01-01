package com.wixsite.mupbam1.b9_eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class B9EurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(B9EurekaApplication.class, args);
	}

}
