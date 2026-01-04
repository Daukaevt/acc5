package com.wixsite.mupbam1.b9_gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {"eureka.client.enabled=false"})
class B9GatewayApplicationTests {

	@Test
	void contextLoads() {
	}

}
