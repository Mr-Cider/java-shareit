package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemStorage;
import ru.practicum.shareit.user.model.User;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@Sql(scripts = "/data.sql")
@DisplayName("Item Storage Tests")
public class ItemStorageTest {
    @Autowired
    private ItemStorage itemStorage;
    User owner;

    @BeforeEach
    void setUp() {
        owner = User.builder()
                .id(1L)
                .email("owner@email.com")
                .name("ownerName")
                .build();
    }

    @DisplayName("Получение вещей по id пользователя")
    @Test
    public void shouldFindAllByOwnerId() {
        List<Item> items = itemStorage.findAllByOwnerId(1L);
        assertThat(items).hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("name", "itemName")
                .hasFieldOrPropertyWithValue("description", "itemDescription");
    }

    @DisplayName("Поиск вещей")
    @Test
    public void shouldSearchItems() {
        List<Item> items = itemStorage.searchItems("item");
        assertThat(items)
                .hasSize(2)
                .first()
                .hasFieldOrPropertyWithValue("name", "itemName")
                .hasFieldOrPropertyWithValue("description", "itemDescription");
    }

    @DisplayName("Найти вещь по id владельца и вещи")
    @Test
    public void shouldFindByIdAndOwnerId() {
        Item findItem = itemStorage.findByIdAndOwner_Id(1L, 1L).orElse(null);
        assertThat(findItem)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", "itemName")
                .hasFieldOrPropertyWithValue("owner.id", 1L);
    }

    @DisplayName("Найти вещи по id запроса")
    @Test
    public void shouldFindByRequestId() {
        List<Item> findByRequest_Id = itemStorage.findByRequest_Id(1L);
        assertThat(findByRequest_Id)
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("name", "itemName")
                .hasFieldOrPropertyWithValue("request.id", 1L);
    }
}
