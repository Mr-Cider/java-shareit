package ru.practicum.shareit.item;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.repository.CommentRepository;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@ActiveProfiles("test")
@DataJpaTest
@Sql(scripts = "/data.sql")
@DisplayName("Comment Repository Tests")
public class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @DisplayName("Поиск по id вещи")
    @Test
    public void shouldFindByItemId() {
        List<Comment> comments = commentRepository.findByItemId(1L);
        assertThat(comments).hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("text", "commentForTest");
    }
}
