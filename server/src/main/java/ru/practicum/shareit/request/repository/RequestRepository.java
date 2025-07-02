package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.request.model.ItemRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RequestRepository extends JpaRepository<ItemRequest, Long> {

    @Query("SELECT r FROM ItemRequest r WHERE r.requestor.id = :requestorId")
    Page<ItemRequest> getRequestsByRequestorId(@Param("requestorId") Long requestorId,
                                               Pageable pageable);

    @Query("SELECT r FROM ItemRequest r WHERE r.requestor.id != :requestorId")
    List<ItemRequest> getOtherRequests(@Param ("requestorId") Long requestorId);
}
