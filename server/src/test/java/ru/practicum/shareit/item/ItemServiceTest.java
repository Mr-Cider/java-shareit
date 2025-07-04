package ru.practicum.shareit.item;

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
import ru.practicum.shareit.exception.IncorrectAccessException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("Item Service Tests")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ItemServiceTest {

    private final ItemService itemService;
    private final EntityManager em;
    private NewItemDto newItemDto;
    private UpdateItemDto updateItemDto;
    private ItemDto itemDto;
    private NewCommentDto newCommentDto;


    @BeforeEach
    void setUp() {
        newItemDto = NewItemDto.builder()
                .name("itemNameForCreate")
                .description("itemDescriptionForCreate")
                .available(true)
                .build();
        updateItemDto = UpdateItemDto.builder()
                .id(1L)
                .name("itemNameForUpdate")
                .description("itemDescriptionForUpdate")
                .available(true)
                .build();

        itemDto = ItemDto.builder()
                .id(1L)
                .name("itemName")
                .description("itemDescription")
                .available(true)
                .build();

        newCommentDto = NewCommentDto.builder()
                .text("textForComment")
                .build();
    }

    @DisplayName("Добавление вещи")
    @Test
    public void shouldAddItem() {
        itemService.addItem(1L, newItemDto);
        TypedQuery<Item> query = em.createQuery("SELECT i FROM Item i WHERE i.name = :name", Item.class);
        Item responseQuery = query.setParameter("name", newItemDto.getName()).getSingleResult();
        assertThat(responseQuery)
                .isNotNull()
                .satisfies(i -> {
                    assertThat(i.getId()).isNotNull();
                    assertThat(i.getName()).isEqualTo(newItemDto.getName());
                    assertThat(i.getDescription()).isEqualTo(newItemDto.getDescription());
                    assertThat(i.getAvailable()).isEqualTo(newItemDto.getAvailable());
                });
    }

    @DisplayName("Создание вещи при некорректном id пользователя")
    @Test
    public void shouldCreateItemWithOwnerIncorrect() {
        assertThatThrownBy(() -> itemService.addItem(999L, newItemDto)).isInstanceOf(NotFoundException.class);
    }

    @DisplayName("Обновление вещи")
    @Test
    public void shouldUpdateItem() {
        TypedQuery<Item> query = em.createQuery("SELECT i FROM Item i WHERE i.id = :id", Item.class);
        Item oldItem = query.setParameter("id", 1).getSingleResult();
        assertThat(oldItem).isNotNull()
                .satisfies(i -> {
                    assertThat(i.getId()).isEqualTo(1);
                    assertThat(i.getName()).isEqualTo(itemDto.getName());
                    assertThat(i.getDescription()).isEqualTo(itemDto.getDescription());
                    assertThat(i.getAvailable()).isEqualTo(itemDto.getAvailable());
                });
        itemService.updateItem(1L, updateItemDto);
        Item updateItem = query.setParameter("id", 1).getSingleResult();
        assertThat(updateItem)
                .isNotNull()
                .satisfies(i -> {
                    assertThat(i.getId()).isEqualTo(1);
                    assertThat(i.getName()).isEqualTo(updateItemDto.getName());
                    assertThat(i.getDescription()).isEqualTo(updateItemDto.getDescription());
                    assertThat(i.getAvailable()).isEqualTo(updateItemDto.getAvailable());
                });
    }

    @DisplayName("Обновление вещи с некорректным id")
    @Test
    public void shouldUpdateNonExistantItem() {
        updateItemDto.setId(999L);
        assertThatThrownBy(() ->
                itemService.updateItem(1L, updateItemDto)).isInstanceOf(NotFoundException.class);
    }

    @DisplayName("Обновление вещи при некорректном id пользователя")
    @Test
    public void shouldUpdateItemByNonExistantOwner() {
        assertThatThrownBy(() ->
                itemService.updateItem(999L, updateItemDto)).isInstanceOf(NotFoundException.class);
    }

    @DisplayName("Получение вещи по id владельца и вещи")
    @Test
    public void shouldGetItem() {
        ItemWithBookingsDto getItemDto = itemService.getItem(1L, 1L);
        assertThat(getItemDto).isNotNull()
                .satisfies(i -> {
                    assertThat(i.getId()).isEqualTo(itemDto.getId());
                    assertThat(i.getName()).isEqualTo(itemDto.getName());
                    assertThat(i.getDescription()).isEqualTo(itemDto.getDescription());
                    assertThat(i.getAvailable()).isEqualTo(itemDto.getAvailable());
                    assertThat(i.getNextBooking()).isNotNull();
                    assertThat(i.getLastBooking()).isNotNull();
                    assertThat(i.getComments()).hasSize(1);
                });
    }

    @DisplayName("Получение вещи для постороннего пользователя")
    @Test
    public void shouldGetItemByAnotherOwner() {
        ItemWithBookingsDto getItemDto = itemService.getItem(1L, 2L);
        assertThat(getItemDto).isNotNull()
                .satisfies(i -> {
                    assertThat(i.getId()).isEqualTo(1L);
                    assertThat(i.getName()).isEqualTo(itemDto.getName());
                    assertThat(i.getDescription()).isEqualTo(itemDto.getDescription());
                    assertThat(i.getAvailable()).isEqualTo(itemDto.getAvailable());
                    assertThat(i.getNextBooking()).isNull();
                    assertThat(i.getLastBooking()).isNull();
                    assertThat(i.getComments()).hasSize(1);
                });
    }

    @DisplayName("Получение всех вещей пользователя")
    @Test
    public void shouldGetUserItems() {
        List<ItemDto> items = itemService.getUserItems(1L);
        assertThat(items).hasSize(1);
    }

    @DisplayName("Поиск вещей")
    @Test
    public void shouldSearchItems() {
        List<ItemDto> items = itemService.searchItems("Description");
        assertThat(items).hasSize(2);
    }

    @DisplayName("Добавление комментария")
    @Test
    public void shouldAddComment() {
        NewCommentDto newCommentDto = NewCommentDto.builder()
                .text("textForComment")
                .build();
        itemService.addComment(2L, 1L, newCommentDto);
    }

    @DisplayName("Добавление комментария пользователя, не бравшего вещь в аренду")
    @Test
    public void shouldAddCommentByAnotherUser() {
        User anotherUser = User.builder()
                .email("anotherUser@test.ru")
                .name("anotherUserName")
                .build();
        em.persist(anotherUser);
        em.flush();
        assertThatThrownBy(() -> itemService.addComment(anotherUser.getId(), 1L, newCommentDto))
                .isInstanceOf(IncorrectAccessException.class);
    }
}
