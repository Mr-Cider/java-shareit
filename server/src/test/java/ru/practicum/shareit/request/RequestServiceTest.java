package ru.practicum.shareit.request;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestWithResponseDto;
import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.RequestService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

@Transactional
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("Request Service Tests")
@SpringBootTest
public class RequestServiceTest {
    private final RequestService requestService;
    private final EntityManager em;
    private NewRequestDto newRequestDto;

    @BeforeEach
    void setUp() {
        newRequestDto = NewRequestDto.builder()
                .description("newRequestDescription")
                .build();
    }

    @DisplayName("Создание запроса вещи")
    @Test
    public void shouldCreateRequest() {
        requestService.createRequest(1L, newRequestDto);
        TypedQuery<ItemRequest> query = em.createQuery(
                "SELECT r FROM ItemRequest r WHERE r.description = :description", ItemRequest.class);
        ItemRequest responseQuery = query.setParameter(
                "description", newRequestDto.getDescription()).getSingleResult();
        assertThat(responseQuery.getDescription()).isEqualTo(newRequestDto.getDescription());
    }

    @DisplayName("Создание запроса вещи несуществующим пользователем")
    @Test
    public void createRequestByUserNoneExist() {
        assertThatThrownBy(() -> requestService.createRequest(999L, newRequestDto))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("Получение запросов пользователя")
    @Test
    public void shouldGetUserRequests() {
        List<ItemRequestWithResponseDto> requests = requestService.getUserRequests(1L);
        assertThat(requests)
                .hasSize(1)
                .first()
                .satisfies(r -> {
                    assertThat(r.getDescription()).isEqualTo("requestDescription");
                    assertThat(r.getItems()).hasSize(1);
                    assertThat(r.getCreated()).isNotNull();
                    assertThat(r.getRequestor().getId()).isEqualTo(1L);
                });
    }

    @DisplayName("Получение прочих запросов")
    @Test
    public void shouldGetAllRequests() {
        List<ItemRequestWithResponseDto> requests = requestService.getAllRequests(1L);
        assertThat(requests)
                .hasSize(1)
                .first()
                .satisfies(r -> {
                    assertThat(r.getDescription()).isEqualTo("requestDescription2");
                    assertThat(r.getItems()).hasSize(1);
                    assertThat(r.getCreated()).isNotNull();
                    assertThat(r.getRequestor().getId()).isEqualTo(2L);
                });
    }

    @DisplayName("Получение запроса по id")
    @Test
    public void shouldGetRequestById() {
        ItemRequestWithResponseDto request = requestService.getRequestById(1L);
        assertThat(request)
        .isNotNull()
                .satisfies(r -> {
                    assertThat(r.getDescription()).isEqualTo("requestDescription");
                    assertThat(r.getItems()).hasSize(1);
                    assertThat(r.getCreated()).isNotNull();
                    assertThat(r.getRequestor().getId()).isEqualTo(1L);
                });
    }

    @DisplayName("Получение запроса по некорректному id")
    @Test
    public void shouldGetRequestByIncorrectId() {
        assertThatThrownBy(() -> requestService.getRequestById(999L))
                .isInstanceOf(NotFoundException.class);
    }
}
