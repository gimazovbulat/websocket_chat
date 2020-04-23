package ru.itis.dao.interfaces;

import ru.itis.models.User;

import java.util.Optional;

public interface UsersRepository extends CrudRepository<Long, User> {
    Optional<User> findByEmail(String email);

    Optional<User> findByConfirmLink(String confirmLink);
}
