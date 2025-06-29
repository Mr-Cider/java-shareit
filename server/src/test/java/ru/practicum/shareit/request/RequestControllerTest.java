package ru.practicum.shareit.request;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.controller.RequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithResponseDto;
import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.ShareItServerTests.X_SHARER_USER_ID;

@AutoConfigureMockMvc
@WebMvcTest(controllers = RequestController.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
@DisplayName("Request Controller Tests")
public class RequestControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RequestService requestService;

    private NewRequestDto newRequestDto;
    private ItemRequestDto requestDto;
    private UserDto firstUserDto;
    private UserDto secondUserDto;
    private ItemRequestWithResponseDto firstRequestWithResponseDto;
    private ItemRequestWithResponseDto secondRequestWithResponseDto;
    private ItemRequestWithResponseDto thirdRequestWithResponseDto;
    private List<ItemRequestWithResponseDto> listFirstUserWithResponseDto;
    private List<ItemRequestWithResponseDto> listAllUserWithResponseDto;

    @BeforeEach
    void setUp() {
        firstUserDto = UserDto.builder()
                .id(1L)
                .name("name")
                .email("test@test.ru")
                .build();

        secondUserDto = UserDto.builder()
                .id(1L)
                .name("nameToo")
                .email("test2@test.ru")
                .build();

        newRequestDto = NewRequestDto.builder()
                .description("requestDescription")
                .build();

        requestDto = ItemRequestDto.builder()
                .id(1L)
                .description("requestDescription")
                .created(LocalDateTime.now())
                .requestor(firstUserDto)
                .build();

        firstRequestWithResponseDto = ItemRequestWithResponseDto.builder()
                .id(1L)
                .description("firstRequestDescription")
                .created(LocalDateTime.now())
                .requestor(firstUserDto)
                .build();

        secondRequestWithResponseDto = ItemRequestWithResponseDto.builder()
                .id(2L)
                .description("secondRequestDescription")
                .created(LocalDateTime.now())
                .requestor(firstUserDto)
                .build();

        thirdRequestWithResponseDto = ItemRequestWithResponseDto.builder()
                .id(3L)
                .description("thirdRequestDescription")
                .created(LocalDateTime.now())
                .requestor(secondUserDto)
                .build();

        listFirstUserWithResponseDto = Arrays.asList(firstRequestWithResponseDto, secondRequestWithResponseDto);
        listAllUserWithResponseDto = Collections.singletonList(thirdRequestWithResponseDto);
    }

    @DisplayName("Создание пользователя")
    @Test
    public void shouldCreateRequest() throws Exception {
        when(requestService.createRequest(firstUserDto.getId(), newRequestDto)).thenReturn(requestDto);
        mockMvc.perform(post("/requests").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRequestDto))
                        .header(X_SHARER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value(requestDto.getDescription()))
                .andExpect(jsonPath("$.created").exists())
                .andExpect(jsonPath("$.requestor.id").value(firstUserDto.getId()))
                .andExpect(jsonPath("$.requestor.name").value(requestDto.getRequestor().getName()))
                .andExpect(jsonPath("$.requestor.email").value(requestDto.getRequestor().getEmail()));
    }

    @DisplayName("Получение запросов пользователя")
    @Test
    public void shouldGetUserRequests() throws Exception {
        when(requestService.getUserRequests(firstUserDto.getId())).thenReturn(listFirstUserWithResponseDto);
        mockMvc.perform(get("/requests")
                        .header(X_SHARER_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @DisplayName("Получение всех чужих запросов пользователей для пользователя с id 1")
    @Test
    public void shouldGetAllRequestsForFirstUser() throws Exception {
        when(requestService.getAllRequests(firstUserDto.getId())).thenReturn(listAllUserWithResponseDto);
        mockMvc.perform(get("/requests/all")
                        .header(X_SHARER_USER_ID, firstUserDto.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @DisplayName("Получение всех чужих запросов пользователей для пользователя с id 2")
    @Test
    public void shouldGetAllRequestsForSecondUser() throws Exception {
        when(requestService.getAllRequests(secondUserDto.getId())).thenReturn(listFirstUserWithResponseDto);
        mockMvc.perform(get("/requests/all")
                        .header(X_SHARER_USER_ID, secondUserDto.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @DisplayName("Получение запроса по id")
    @Test
    public void shouldGetRequest() throws Exception {
        when(requestService.getRequestById(firstRequestWithResponseDto.getId()))
                .thenReturn(firstRequestWithResponseDto);
        mockMvc.perform(get("/requests/{id}", firstRequestWithResponseDto.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(firstRequestWithResponseDto.getId()))
                .andExpect(jsonPath("$.description").value(firstRequestWithResponseDto.getDescription()))
                .andExpect(jsonPath("$.created").exists())
                .andExpect(jsonPath("$.requestor.id").value(firstUserDto.getId()))
                .andExpect(jsonPath("$.requestor.name").value(requestDto.getRequestor().getName()))
                .andExpect(jsonPath("$.requestor.email").value(requestDto.getRequestor().getEmail()));
    }
}
