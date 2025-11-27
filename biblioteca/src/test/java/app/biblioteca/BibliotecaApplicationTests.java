package app.biblioteca;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
	    "secret_key=test123"    // or "secret.key=test123"
	})
class BibliotecaApplicationTests {

	@Test
	void contextLoads() {
	}

}
