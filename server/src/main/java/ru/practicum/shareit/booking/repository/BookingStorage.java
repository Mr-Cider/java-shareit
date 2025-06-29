package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingStorage extends JpaRepository<Booking, Long> {

    @Modifying
    @Query("UPDATE Booking b SET b.bookingStatus = :status WHERE b.item.owner.id = :userId AND b.id = :bookingId")
    int approveOrRejectBooking(@Param("userId") Long userId,
                                   @Param ("bookingId") Long bookingId,
                                   @Param ("status") BookingStatus status);

    @Query("SELECT b FROM Booking b " +
            "JOIN b.item i WHERE b.id = :bookingId AND (b.booker.id = :userId OR i.owner.id = :userId)")
    Optional<Booking> getBookingApproved(@Param("userId") Long userId, @Param("bookingId") Long bookingId);

    List<Booking> findByBooker_Id(Long bookerId);

    List<Booking> findByBookerIdAndBookingStatus(Long bookerId, BookingStatus status);

    @Query("SELECT b FROM Booking b " +
            "JOIN b.item i WHERE b.booker.id = :userId AND b.bookingStatus = :status AND b.startDate > :date ")
    List<Booking> getUserFutureBookings(@Param ("userId") Long userId, @Param("status") BookingStatus status, @Param("date") LocalDateTime date);

    @Query("SELECT b FROM Booking b " +
            "JOIN b.item i WHERE b.booker.id = :userId AND b.bookingStatus = :status AND b.endDate < :date ")
    List<Booking> getUserPastBookings(@Param ("userId") Long userId, @Param("status") BookingStatus status, @Param("date") LocalDateTime date);

    @Query("SELECT b FROM Booking b " +
            "JOIN b.item i WHERE b.booker.id = :userId AND b.endDate < :date")
    List<Booking> getUserPastBookingsAllStatus(@Param ("userId") Long userId, @Param("date") LocalDateTime date);

    @Query("SELECT b FROM Booking b " +
            "JOIN b.item i WHERE b.booker.id = :userId AND b.bookingStatus = :status AND (:date BETWEEN b.startDate AND b.endDate)")
    List<Booking> getUserCurrentBookings(@Param ("userId") Long userId, @Param("status") BookingStatus status, @Param("date") LocalDateTime date);

    @Query("SELECT b FROM Booking b " +
            "JOIN b.item i WHERE i.owner.id = :ownerId AND b.bookingStatus = :status AND (:date BETWEEN b.startDate AND b.endDate)")
    List<Booking> getOwnerCurrentBookings(@Param ("ownerId") Long ownerId, @Param("status") BookingStatus status, @Param("date") LocalDateTime date);

    @Query("SELECT b FROM Booking b " +
            "JOIN b.item i WHERE i.owner.id = :ownerId AND b.bookingStatus = :status AND b.endDate < :date ")
    List<Booking> getOwnerPastBookings(@Param ("ownerId") Long ownerId, @Param("status") BookingStatus status, @Param("date") LocalDateTime date);

    @Query("SELECT b FROM Booking b " +
            "JOIN b.item i WHERE i.owner.id = :ownerId AND b.bookingStatus = :status AND b.startDate > :date ")
    List<Booking> getOwnerFutureBookings(@Param ("ownerId") Long ownerId, @Param("status") BookingStatus status, @Param("date") LocalDateTime date);

    @Query("SELECT b FROM Booking b " +
            "JOIN b.item i WHERE i.owner.id = :ownerId AND b.bookingStatus = :status ")
    List<Booking> getOwnerBookingsWithStatus(@Param ("ownerId") Long ownerId, @Param("status") BookingStatus status);

    @Query("SELECT b FROM Booking b " +
            "JOIN b.item i WHERE i.owner.id = :ownerId ")
    List<Booking> getOwnerAllBookings(@Param("ownerId") Long ownerId);


    List<Booking> findByItemId(@Param("itemId") Long itemId);
}
