package ru.itis.dao.impl;

import org.springframework.stereotype.Repository;
import ru.itis.dao.interfaces.ChatRoomRepository;
import ru.itis.models.ChatRoom;
import ru.itis.models.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class ChatRoomRepositoryImpl implements ChatRoomRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ChatRoom> findRooms(User user) {
        return entityManager
                .createQuery("select chatRoom from ChatRoom chatRoom join chatRoom.chatters chatter where chatter = ?1")
                .setParameter(1, user)
                .getResultList();
    }

    @Override
    public Optional<ChatRoom> findById(Long id) {
        ChatRoom chatRoom = (ChatRoom) entityManager.createQuery("select chatRoom from ChatRoom chatRoom where chatRoom.id = ?1")
                .setParameter(1, id)
                .getSingleResult();
        return Optional.of(chatRoom);
    }

    @Override
    public void save(ChatRoom chatRoom) {
        entityManager.persist(chatRoom);
    }

    @Override
    public List<ChatRoom> findAll() {
        List<ChatRoom> roomsForAdmin =
                entityManager.createQuery("select room from ChatRoom room")
                        .getResultList();
        return roomsForAdmin;
    }

    @Override
    public void update(ChatRoom chatRoom) {
        entityManager.merge(chatRoom);
    }
}
