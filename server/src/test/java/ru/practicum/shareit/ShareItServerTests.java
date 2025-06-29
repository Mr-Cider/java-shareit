package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ShareItServerTests {
	public static String X_SHARER_USER_ID = "X-Sharer-User-Id";

	@Test
	void contextLoads() {
	}
}
