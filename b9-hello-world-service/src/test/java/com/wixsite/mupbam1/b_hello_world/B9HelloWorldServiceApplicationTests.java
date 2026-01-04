package com.wixsite.mupbam1.b_hello_world;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {"eureka.client.enabled=false"})
class B9HelloWorldServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
