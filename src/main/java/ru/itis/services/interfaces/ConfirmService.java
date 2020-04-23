package ru.itis.services.interfaces;

import ru.itis.dto.UserDto;

public interface ConfirmService {
    UserDto confirm(String confirmLink);
}
