package ru.itis.dao.impl;

import org.springframework.stereotype.Repository;
import ru.itis.dao.interfaces.ChatMessagesRepository;
import ru.itis.models.ChatMessage;
import ru.itis.models.ChatRoom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;

@Repository
public class ChatMessagesRepositoryImpl implements ChatMessagesRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ChatMessage> findAll(ChatRoom chatRoom) {
        List<ChatMessage> messages = entityManager
                .createQuery("select message from ChatMessage message where message.chatRoom = ?1")
                .setParameter(1, chatRoom)
                .getResultList();
        if (messages.size() == 0) {
            return Collections.emptyList();
        }
        return messages;
    }

    @Override
    public void save(ChatMessage chatMessage) {
        entityManager.persist(chatMessage);
    }
}
