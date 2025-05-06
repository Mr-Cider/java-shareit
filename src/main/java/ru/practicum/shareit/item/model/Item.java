package ru.practicum.shareit.item.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
@RequiredArgsConstructor
public class Item {
    private Long id;
    private Long ownerId;
    private String name;
    private String description;
    @Getter
    private Long status;
    @JsonIgnore
    private transient ItemStatus itemStatus;


    @JsonIgnore
    public ItemStatus getItemStatus() {
        return ItemStatus.getStatusById(this.status != null ? this.status : 1L);
    }

    @JsonProperty("status")
    public Map<String, Object> getStatusAsMap() {
        ItemStatus status = getItemStatus();
        return Map.of("id", status.getId(), "status", status.getDisplayStatus());
    }
}
