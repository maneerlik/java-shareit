package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validation.UpdateValidation;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @JsonInclude
    private Long id;

    @NotBlank(message = "Name cannot be empty", groups = Default.class)
    private String name;

    @NotBlank(message = "Email cannot be empty", groups = Default.class)
    @Email(
            message = "Email must contain '@' and a valid domain name",
            groups = {Default.class, UpdateValidation.class}
    )
    private String email;
}
