package ru.itis.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.dao.interfaces.ChatMessagesRepository;
import ru.itis.dao.interfaces.ChatRoomRepository;
import ru.itis.dto.ChatMessageDto;
import ru.itis.models.ChatMessage;
import ru.itis.models.ChatRoom;
import ru.itis.services.interfaces.ChatMessagesService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatMessageServiceImpl implements ChatMessagesService {
    private final ChatMessagesRepository chatMessagesRepository;
    private final ChatRoomRepository chatRoomRepository;

    public ChatMessageServiceImpl(ChatMessagesRepository chatMessagesRepository,
                                  ChatRoomRepository chatRoomRepository) {
        this.chatMessagesRepository = chatMessagesRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    @Override
    public List<ChatMessageDto> findAll(Long roomId) {
        Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findById(roomId);
        if (chatRoomOptional.isPresent()) {
            ChatRoom chatRoom = chatRoomOptional.get();
            return chatMessagesRepository
                    .findAll(chatRoom)
                    .stream()
                    .map(ChatMessage::toChatMessageDto)
                    .collect(Collectors.toList());

        }
        throw new IllegalStateException("room doesn't exist");
    }

    @Transactional
    @Override
    public ChatMessageDto save(ChatMessageDto chatMessageDto) {
        ChatMessage chatMessage = ChatMessage.fromChatMessageDto(chatMessageDto);
        chatMessagesRepository.save(chatMessage);
        return ChatMessage.toChatMessageDto(chatMessage);
    }
}