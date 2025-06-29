package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShareItGatewayTest {
    public static String X_SHARER_USER_ID = "X-Sharer-User-Id";
    @Test
    void contextLoads() {
    }
}
