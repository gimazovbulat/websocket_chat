package ru.itis.services.interfaces;

import ru.itis.dto.SignUpForm;
import ru.itis.dto.UserDto;

public interface SignUpService {
    UserDto signUp(SignUpForm signUpForm);
}
