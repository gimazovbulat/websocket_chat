package ru.itis.services.interfaces;

import ru.itis.dto.ChatRoomDto;
import ru.itis.dto.UserDto;

import java.util.List;

public interface ChatRoomsService {
    List<ChatRoomDto> geAllRoomsForUser(UserDto userDto);
    List<ChatRoomDto> getAll();
    ChatRoomDto getRoom(Long id);
    void createRoom(ChatRoomDto chatRoomDto);
    void update(ChatRoomDto chatRoomDto);
}
