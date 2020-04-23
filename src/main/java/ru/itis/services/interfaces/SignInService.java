package ru.itis.services.interfaces;

import ru.itis.dto.SignInForm;
import ru.itis.dto.TokenDto;

public interface SignInService {
    TokenDto signIn(SignInForm signInForm);
}
