package ru.practicum.shareit.booking;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import jakarta.persistence.TypedQuery;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.NotFoundException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("Booking Service Tests")
@SpringBootTest
public class BookingServiceTest {
    private final BookingService bookingService;

    private final EntityManager em;

    private UserDto firstUserDto;
    private UserDto secondUserDto;
    private ItemDto itemDto;
    private NewBookingDto newBookingDto;
    private BookingDto firstBookingDto;

    @BeforeEach
    void setUp() {
        firstUserDto = UserDto.builder()
                .id(1L)
                .name("firstName")
                .email("firstEmail@test.ru")
                .build();
        secondUserDto = UserDto.builder()
                .id(2L)
                .name("secondName")
                .email("secondEmail@test.ru")
                .build();

        itemDto = ItemDto.builder()
                .id(1L)
                .name("itemName")
                .description("itemDescription")
                .available(true)
                .build();
        newBookingDto = NewBookingDto.builder()
                .start(LocalDateTime.now().plusDays(3))
                .end(LocalDateTime.now().plusDays(5))
                .itemId(itemDto.getId())
                .build();
        firstBookingDto = BookingDto.builder()
                .id(7L)
                .start(newBookingDto.getStart())
                .end(newBookingDto.getEnd())
                .item(itemDto)
                .booker(secondUserDto)
                .status("WAITING")
                .build();
    }

    @DisplayName("Создание бронирования")
    @Test
    public void shouldCreateBooking() {
        bookingService.createBooking(secondUserDto.getId(), newBookingDto);
        TypedQuery<Booking> query = em.createQuery("SELECT b FROM Booking b WHERE b.id = :id", Booking.class);
        Booking responseQuery = query.setParameter("id", firstBookingDto.getId()).getSingleResult();
        assertThat(responseQuery)
                .isNotNull()
                .satisfies(booking -> {
                    assertThat(booking.getStartDate()).isEqualTo(newBookingDto.getStart());
                    assertThat(booking.getEndDate()).isEqualTo(newBookingDto.getEnd());
                    assertThat(booking.getBooker().getId()).isEqualTo(secondUserDto.getId());
                    assertThat(booking.getItem().getId()).isEqualTo(itemDto.getId());
                });
    }

    @DisplayName("Создание бронирования у пользователя, которого нет в бд")
    @Test
    public void notShouldCreateBookingByIncorrectUserId() {
        assertThatThrownBy(() ->
                bookingService.createBooking(999L, newBookingDto))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("Создание бронирования с вещью, которой нет в бд")
    @Test
    public void notShouldCreateBookingByIncorrectItemId() {
        newBookingDto.setItemId(999L);
        assertThatThrownBy(() ->
                bookingService.createBooking(secondUserDto.getId(), newBookingDto))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("Подтверждение бронирования")
    @Test
    public void shouldApproveOrRejectBookingIsTrue() {
        bookingService.approveOrRejectBooking(firstUserDto.getId(), 3L, true);
        TypedQuery<Booking> query = em.createQuery("SELECT b FROM Booking b WHERE b.id = :id", Booking.class);
        Booking responseQuery = query.setParameter("id", 3L).getSingleResult();
        assertThat(responseQuery).isNotNull()
                .satisfies(booking -> {
                    assertThat(responseQuery.getId()).isEqualTo(3L);
                    assertThat(responseQuery.getStartDate()).isNotNull();
                    assertThat(responseQuery.getEndDate()).isNotNull();
                    assertThat(responseQuery.getBooker().getId()).isEqualTo(2L);
                    assertThat(responseQuery.getBookingStatus()).isEqualTo(BookingStatus.APPROVED);
                });
    }

    @DisplayName("Отклонение бронирования")
    @Test
    public void shouldApproveOrRejectBookingIsFalse() {
        bookingService.approveOrRejectBooking(firstUserDto.getId(), 3L, false);
        TypedQuery<Booking> query = em.createQuery("SELECT b FROM Booking b WHERE b.id = :id", Booking.class);
        Booking responseQuery = query.setParameter("id", 3L).getSingleResult();
        assertThat(responseQuery).isNotNull()
                .satisfies(booking -> {
                    assertThat(responseQuery.getId()).isEqualTo(3L);
                    assertThat(responseQuery.getStartDate()).isNotNull();
                    assertThat(responseQuery.getEndDate()).isNotNull();
                    assertThat(responseQuery.getBooker().getId()).isEqualTo(2L);
                    assertThat(responseQuery.getBookingStatus()).isEqualTo(BookingStatus.REJECTED);
                });
    }

    @DisplayName("Проверить подтверждение бронирования владельцем вещи")
    @Test
    public void shouldGetBookingApprovedByOwner() {
        bookingService.getBookingApproved(secondUserDto.getId(), 1L);
        TypedQuery<Booking> query = em.createQuery("SELECT b FROM Booking b WHERE b.id = :id", Booking.class);
        Booking responseQuery = query.setParameter("id", 1L).getSingleResult();
        assertThat(responseQuery).isNotNull()
                .satisfies(booking -> {
                    assertThat(responseQuery.getId()).isEqualTo(1L);
                    assertThat(responseQuery.getStartDate()).isNotNull();
                    assertThat(responseQuery.getEndDate()).isNotNull();
                    assertThat(responseQuery.getBooker().getId()).isEqualTo(2L);
                    assertThat(responseQuery.getBookingStatus()).isEqualTo(BookingStatus.APPROVED);
                });
    }

    @DisplayName("Проверить подтверждение бронирования автором бронирования")
    @Test
    public void shouldGetBookingApprovedByBooker() {
        bookingService.getBookingApproved(firstUserDto.getId(), 1L);
        TypedQuery<Booking> query = em.createQuery("SELECT b FROM Booking b WHERE b.id = :id", Booking.class);
        Booking responseQuery = query.setParameter("id", 1L).getSingleResult();
        assertThat(responseQuery).isNotNull()
                .satisfies(booking -> {
                    assertThat(responseQuery.getId()).isEqualTo(1L);
                    assertThat(responseQuery.getStartDate()).isNotNull();
                    assertThat(responseQuery.getEndDate()).isNotNull();
                    assertThat(responseQuery.getBooker().getId()).isEqualTo(2L);
                    assertThat(responseQuery.getBookingStatus()).isEqualTo(BookingStatus.APPROVED);
                });
    }

    @DisplayName("Проверить подтверждение бронирования сторонним пользователем")
    @Test
    public void notShouldGetBookingApprovedByOtherUser() {
        assertThatThrownBy(() -> bookingService.getBookingApproved(3L, 1L))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("Получить список текущих бронирований")
    @Test
    public void shouldGetBookingStateCurrent() {
        List<BookingDto> bookingState = bookingService.getBookingState(2L, State.CURRENT);
        assertThat(bookingState).isNotNull()
                .hasSize(1)
                .first()
                .satisfies(bookingDto -> {
                    assertThat(bookingDto.getId()).isEqualTo(4L);
                    assertThat(bookingDto.getStart()).isBefore(LocalDateTime.now());
                    assertThat(bookingDto.getEnd()).isAfter(LocalDateTime.now());
                    assertThat(bookingDto.getStatus()).isEqualTo("APPROVED");
                });
    }

    @DisplayName("Получить список прошлых бронирований")
    @Test
    public void shouldGetBookingStatePast() {
        List<BookingDto> bookingState = bookingService.getBookingState(2L, State.PAST);
        assertThat(bookingState).isNotNull()
                .hasSize(1)
                .first()
                .satisfies(bookingDto -> {
                    assertThat(bookingDto.getId()).isEqualTo(1L);
                    assertThat(bookingDto.getEnd()).isBefore(LocalDateTime.now());
                    assertThat(bookingDto.getStatus()).isEqualTo("APPROVED");
                });
    }

    @DisplayName("Получить список будущих бронирований")
    @Test
    public void shouldGetBookingStateFuture() {
        List<BookingDto> bookingState = bookingService.getBookingState(2L, State.FUTURE);
        assertThat(bookingState).isNotNull()
                .hasSize(1)
                .first()
                .satisfies(bookingDto -> {
                    assertThat(bookingDto.getId()).isEqualTo(6L);
                    assertThat(bookingDto.getStart()).isAfter(LocalDateTime.now());
                    assertThat(bookingDto.getStatus()).isEqualTo("APPROVED");
                });
    }

    @DisplayName("Получить список бронирований со статусом WAITING")
    @Test
    public void shouldGetBookingStateWaiting() {
        List<BookingDto> bookingState = bookingService.getBookingState(2L, State.WAITING);
        assertThat(bookingState).isNotNull()
                .hasSize(1)
                .first()
                .satisfies(bookingDto -> {
                    assertThat(bookingDto.getId()).isEqualTo(3L);
                    assertThat(bookingDto.getStatus()).isEqualTo("WAITING");
                });
    }

    @DisplayName("Получить список бронирований со статусом REJECTED")
    @Test
    public void shouldGetBookingStateRejected() {
        List<BookingDto> bookingState = bookingService.getBookingState(2L, State.REJECTED);
        assertThat(bookingState).isNotNull()
                .hasSize(2)
                .first()
                .satisfies(bookingDto -> {
                    assertThat(bookingDto.getId()).isEqualTo(2L);
                    assertThat(bookingDto.getStatus()).isEqualTo("REJECTED");
                });
    }

    @DisplayName("Получить список бронирований со всеми статусами")
    @Test
    public void shouldGetBookingStateAll() {
        List<BookingDto> bookingState = bookingService.getBookingState(2L, State.ALL);
        assertThat(bookingState).isNotNull()
                .hasSize(6);
    }

    @DisplayName("Получить список текущих бронирований")
    @Test
    public void shouldGetOwnerBookingStateCurrent() {
        List<BookingDto> bookingState = bookingService.getOwnerBookingState(1L, State.CURRENT);
        assertThat(bookingState).isNotNull()
                .hasSize(1)
                .first()
                .satisfies(bookingDto -> {
                    assertThat(bookingDto.getId()).isEqualTo(4L);
                    assertThat(bookingDto.getStart()).isBefore(LocalDateTime.now());
                    assertThat(bookingDto.getEnd()).isAfter(LocalDateTime.now());
                    assertThat(bookingDto.getStatus()).isEqualTo("APPROVED");
                });
    }

    @DisplayName("Получить список прошлых бронирований владельца вещи")
    @Test
    public void shouldGetOwnerBookingStatePast() {
        List<BookingDto> bookingState = bookingService.getOwnerBookingState(1L, State.PAST);
        assertThat(bookingState).isNotNull()
                .hasSize(1)
                .first()
                .satisfies(bookingDto -> {
                    assertThat(bookingDto.getId()).isEqualTo(1L);
                    assertThat(bookingDto.getEnd()).isBefore(LocalDateTime.now());
                    assertThat(bookingDto.getStatus()).isEqualTo("APPROVED");
                });
    }

    @DisplayName("Получить список будущих бронирований владельца вещи")
    @Test
    public void shouldGetOwnerBookingStateFuture() {
        List<BookingDto> bookingState = bookingService.getOwnerBookingState(1L, State.FUTURE);
        assertThat(bookingState).isNotNull()
                .hasSize(1)
                .first()
                .satisfies(bookingDto -> {
                    assertThat(bookingDto.getId()).isEqualTo(6L);
                    assertThat(bookingDto.getStart()).isAfter(LocalDateTime.now());
                    assertThat(bookingDto.getStatus()).isEqualTo("APPROVED");
                });
    }

    @DisplayName("Получить список бронирований со статусом WAITING владельца вещи")
    @Test
    public void shouldGetOwnerBookingStateWaiting() {
        List<BookingDto> bookingState = bookingService.getOwnerBookingState(1L, State.WAITING);
        assertThat(bookingState).isNotNull()
                .hasSize(1)
                .first()
                .satisfies(bookingDto -> {
                    assertThat(bookingDto.getId()).isEqualTo(3L);
                    assertThat(bookingDto.getStatus()).isEqualTo("WAITING");
                });
    }

    @DisplayName("Получить список бронирований со статусом REJECTED владельца вещи")
    @Test
    public void shouldGetOwnerBookingStateRejected() {
        List<BookingDto> bookingState = bookingService.getOwnerBookingState(1L, State.REJECTED);
        assertThat(bookingState).isNotNull()
                .hasSize(2)
                .first()
                .satisfies(bookingDto -> {
                    assertThat(bookingDto.getId()).isEqualTo(2L);
                    assertThat(bookingDto.getStatus()).isEqualTo("REJECTED");
                });
    }

    @DisplayName("Получить список бронирований со всеми статусами владельца вещи")
    @Test
    public void shouldGetOwnerBookingStateAll() {
        List<BookingDto> bookingState = bookingService.getOwnerBookingState(1L, State.ALL);
        assertThat(bookingState).isNotNull()
                .hasSize(6);
    }
}
