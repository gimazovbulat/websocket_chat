package ru.itis.services.interfaces;

import ru.itis.dto.ChatMessageDto;

import java.util.List;

public interface ChatMessagesService {
    List<ChatMessageDto> findAll(Long roomId);

    ChatMessageDto save(ChatMessageDto chatMessageDto);
}
