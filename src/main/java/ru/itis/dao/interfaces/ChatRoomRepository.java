package ru.itis.dao.interfaces;

import ru.itis.models.ChatRoom;
import ru.itis.models.User;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository {
    List<ChatRoom> findRooms(User user);

    List<ChatRoom> findAll();

    Optional<ChatRoom> findById(Long id);

    void save(ChatRoom chatRoom);

    void update(ChatRoom chatRoom);
}
