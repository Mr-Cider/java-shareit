package ru.practicum.shareit.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@Sql(scripts = "/data.sql")
@DisplayName("Request Repository Tests")
public class RequestRepositoryTest {
    @Autowired
    private RequestRepository requestRepository;

    @DisplayName("Получить запрос по id пользователя")
    @Test
    public void shouldGetRequestsByRequestorId() {
        List<ItemRequest> requests = requestRepository.getRequestsByRequestorId(1L);
        assertThat(requests).hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("description", "requestDescription");
    }

    @DisplayName("Получить прочие запросы")
    @Test
    public void shouldGetOtherRequests() {
        List<ItemRequest> requests = requestRepository.getOtherRequests(1L);
        assertThat(requests).hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("description", "requestDescription2");
    }
}

