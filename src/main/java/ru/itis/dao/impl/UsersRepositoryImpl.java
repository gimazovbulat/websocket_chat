package ru.itis.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.dao.interfaces.UsersRepository;
import ru.itis.models.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class UsersRepositoryImpl implements UsersRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<User> findByEmail(String email) {
        User user = (User) entityManager.createQuery("select user from User user where user.email = ?1")
                .setParameter(1, email)
                .getSingleResult();

        return Optional.of(user);
    }

    @Override
    public Optional<User> findByConfirmLink(String confirmLink) {
        User user = (User) entityManager.createQuery("select user from User user where user.confirmLink = ?1")
                .setParameter(1, confirmLink)
                .getSingleResult();

        return Optional.of(user);
    }

    @Override
    public Long save(User user) {
        entityManager.persist(user);
        return user.getId();
    }

    @Override
    public Optional<User> find(Long id) {
        User user = (User) entityManager.createQuery("select user FROM User user where user.id = ?1")
                .setParameter(1, id)
                .getSingleResult();

        return Optional.of(user);
    }

    @Override
    public void update(User user) {
        entityManager.merge(user);
    }

    @Override
    public void delete(Long aLong) {

    }
}
