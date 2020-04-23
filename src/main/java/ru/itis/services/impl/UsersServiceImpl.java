package ru.itis.services.impl;

import org.springframework.stereotype.Service;
import ru.itis.dao.interfaces.UsersRepository;
import ru.itis.dto.UserDto;
import ru.itis.models.User;
import ru.itis.services.interfaces.UsersService;

import java.util.Optional;

@Service
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;

    public UsersServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDto findUser(Long id) {
        Optional<User> optionalUser = usersRepository.find(id);
        if (optionalUser.isPresent()) {
            return optionalUser.map(User::toUserDto).get();
        }
        throw new IllegalStateException();
    }

    @Override
    public UserDto findUser(String email) {
        Optional<User> optionalUser = usersRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            return optionalUser.map(User::toUserDto).get();
        }
        throw new IllegalStateException();
    }
}