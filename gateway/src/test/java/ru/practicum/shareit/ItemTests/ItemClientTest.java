package ru.practicum.shareit.ItemTests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static ru.practicum.shareit.ShareItGatewayTest.X_SHARER_USER_ID;

@DisplayName("Item Client Tests")
@SpringBootTest
public class ItemClientTest {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ItemClient itemClient;

    private MockRestServiceServer mockServer;

    UserDto userDto;

    NewItemDto correctNewItemDto;

    ItemDto correctItemDto;

    List<ItemDto> correctItemDtoList;

    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = itemClient.getRestTemplate();
        mockServer = MockRestServiceServer.createServer(restTemplate);

        userDto = UserDto.builder()
                .id(1L)
                .email("test@test.com")
                .name("name")
                .build();
        correctNewItemDto = NewItemDto.builder()
                .name("correctItemName")
                .description("correctDescription")
                .available(true)
                .build();
        correctItemDto = ItemDto.builder()
                .id(1L)
                .name("correctItemName")
                .description("correctDescription")
                .available(true)
                .build();
        correctItemDtoList = List.of(
                ItemDto.builder()
                        .id(1L)
                        .name("firstItemName")
                        .description("firstDescription")
                        .available(true)
                        .build(),
                ItemDto.builder()
                        .id(2L)
                        .name("secondItemName")
                        .description("secondDescription")
                        .available(true)
                        .build()

        );
    }

    @DisplayName("Получение списка вещей пользователя")
    @Test
    public void shouldGetUserItems() throws Exception {
        mockServer.expect(requestTo("http://localhost:9090/items"))
                .andExpect(header(X_SHARER_USER_ID, userDto.getId().toString()))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(objectMapper.writeValueAsString(correctItemDtoList),
                        MediaType.APPLICATION_JSON));
        ResponseEntity<Object> response = itemClient.getUserItems(userDto.getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<ItemDto> responseBody = objectMapper.readValue(objectMapper.writeValueAsString(correctItemDtoList),
                new TypeReference<List<ItemDto>>() {});
        assertThat(responseBody)
                .hasSize(2)
                .isEqualTo(correctItemDtoList);
    }

    @DisplayName("Получение предмета пользователя")
    @Test
    public void shouldGetItem() throws Exception {
        mockServer.expect(requestTo("http://localhost:9090/items/1"))
                .andExpect(header(X_SHARER_USER_ID, userDto.getId().toString()))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(objectMapper.writeValueAsString(correctItemDto), MediaType.APPLICATION_JSON));
        ResponseEntity<Object> response = itemClient.getItem(correctItemDto.getId(), userDto.getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        ItemDto responseBody = objectMapper.convertValue(correctItemDto, ItemDto.class);
        assertThat(responseBody).isEqualTo(correctItemDto);
    }

    @DisplayName("Создание вещи")
    @Test
    public void shouldCreateItem() throws Exception {
        mockServer.expect(requestTo("http://localhost:9090/items"))
                .andExpect(header(X_SHARER_USER_ID, userDto.getId().toString()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(objectMapper.writeValueAsString(correctItemDto), MediaType.APPLICATION_JSON));
        ResponseEntity<Object> response = itemClient.createItem(userDto.getId(), correctNewItemDto);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        ItemDto responseBody = objectMapper.convertValue(response.getBody(), ItemDto.class);
        assertThat(responseBody).isEqualTo(correctItemDto);
    }

    @DisplayName("Обновление вещи")
    @Test
    public void shouldUpdateItem() throws Exception {
        UpdateItemDto updateItemDto = UpdateItemDto.builder()
                .id(1L)
                .name("updatedItemName")
                .description("updatedDescription")
                .available(true)
                .build();
        ItemDto updatedItemDto = ItemDto.builder()
                .id(1L)
                .name("updatedItemName")
                .description("updatedDescription")
                .available(true)
                .build();
        mockServer.expect(requestTo("http://localhost:9090/items/1"))
                .andExpect(header(X_SHARER_USER_ID, userDto.getId().toString()))
                .andExpect(method(HttpMethod.PATCH))
                .andRespond(withSuccess(objectMapper.writeValueAsString(updatedItemDto), MediaType.APPLICATION_JSON));
        ResponseEntity<Object> response = itemClient.updateItem(userDto.getId(), updatedItemDto.getId(), updateItemDto);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        ItemDto responseBody = objectMapper.convertValue(response.getBody(), ItemDto.class);
        assertThat(responseBody).isEqualTo(updatedItemDto);
    }

    @DisplayName("Поиск вещей")
    @Test
    public void shouldSearchItemsByNameOrDescription() throws Exception {
        mockServer.expect(requestTo("http://localhost:9090/items/search?text=ItemName"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(objectMapper.writeValueAsString(correctItemDtoList),
                        MediaType.APPLICATION_JSON));
        ResponseEntity<Object> response = itemClient.searchItemsByNameOrDescription("ItemName");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<ItemDto> responseBody = objectMapper.readValue(objectMapper.writeValueAsString(correctItemDtoList),
                new TypeReference<List<ItemDto>>() {});
        assertThat(responseBody)
                .hasSize(2)
                .isEqualTo(correctItemDtoList);
    }

    @Test
    public void shouldAddComment() throws Exception {
        NewCommentDto newCommentDto = NewCommentDto.builder()
                .text("Вещь!!!!!!!!!")
                .build();
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text(newCommentDto.getText())
                .authorName(userDto.getName())
                .item(correctItemDto)
                .created(LocalDateTime.now())
                .build();
        mockServer.expect(requestTo("http://localhost:9090/items/1/comment"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header(X_SHARER_USER_ID, userDto.getId().toString()))
                .andRespond(withSuccess(objectMapper.writeValueAsString(commentDto), MediaType.APPLICATION_JSON));
        ResponseEntity<Object> response = itemClient.addComment(userDto.getId(), correctItemDto.getId(), newCommentDto);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        CommentDto responseBody = objectMapper.convertValue(response.getBody(), CommentDto.class);
        assertThat(responseBody).isEqualTo(commentDto);
    }
}
