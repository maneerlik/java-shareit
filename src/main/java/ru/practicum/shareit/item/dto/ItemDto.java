package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    @JsonInclude
    private Long id;

    @NotBlank(message = "Name cannot be empty", groups = Default.class)
    private String name;

    @NotBlank(message = "Description cannot be empty", groups = Default.class)
    private String description;

    @NotNull(message = "Available cannot be null", groups = Default.class)
    private Boolean available;

    private Long requestId;
}
