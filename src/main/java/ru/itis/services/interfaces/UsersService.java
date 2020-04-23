package ru.itis.services.interfaces;

import ru.itis.dto.UserDto;

public interface UsersService {
    UserDto findUser(Long id);

    UserDto findUser(String email);
}
