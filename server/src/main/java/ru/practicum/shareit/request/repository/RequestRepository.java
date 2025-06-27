package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface RequestRepository extends JpaRepository<ItemRequest, Long> {

    @Query("SELECT r FROM ItemRequest r WHERE r.requestor.id = :requestorId")
    List<ItemRequest> getRequestsByRequestorId(@Param("requestorId") Long requestorId);

    @Query("SELECT r FROM ItemRequest r WHERE r.requestor.id != :requestorId")
    List<ItemRequest> getOtherRequests(@Param ("requestorId") Long requestorId);
}
