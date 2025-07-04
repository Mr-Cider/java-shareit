package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage extends JpaRepository<Item, Long> {

    List<Item> findAllByOwnerId(Long ownerId);

    @Query("SELECT i FROM Item i WHERE (LOWER(i.name) LIKE LOWER(CONCAT('%', :text ,'%')) OR LOWER(i.description) LIKE LOWER(CONCAT('%', :text ,'%'))) AND i.available = true")
    List<Item> searchItems(@Param("text") String text);

    @Query("SELECT i FROM Item i WHERE i.id = :itemId AND i.owner.id = :userId")
    Optional<Item> findByIdAndOwner_Id(@Param("itemId") Long itemId, @Param("userId") Long userId);

    List<Item> findByRequest_Id(Long requestId);
}
