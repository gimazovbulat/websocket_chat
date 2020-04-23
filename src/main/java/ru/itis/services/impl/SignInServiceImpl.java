package ru.itis.services.impl;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.dao.interfaces.UsersRepository;
import ru.itis.dto.SignInForm;
import ru.itis.dto.TokenDto;
import ru.itis.models.User;
import ru.itis.security.JwtUtils;
import ru.itis.services.interfaces.SignInService;

import java.util.Optional;

@Service
public class SignInServiceImpl implements SignInService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public SignInServiceImpl(UsersRepository usersRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
    @Override
    public TokenDto signIn(SignInForm signInForm) {
        Optional<User> optionalUser = usersRepository.findByEmail(signInForm.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(signInForm.getPassword(), user.getPassword())) {
                String token = jwtUtils.createToken(user);
                return new TokenDto(token);
            } else throw new AccessDeniedException("Wrong email/password");
        } else throw new AccessDeniedException("User not found");
    }
}
