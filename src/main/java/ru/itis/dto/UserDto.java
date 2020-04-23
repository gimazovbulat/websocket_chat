package ru.itis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.itis.models.UserState;

@Data
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String email;
    private UserState userState;
}
