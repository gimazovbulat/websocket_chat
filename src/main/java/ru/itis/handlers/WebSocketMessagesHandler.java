package ru.itis.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.itis.dto.*;
import ru.itis.services.interfaces.ChatMessagesService;
import ru.itis.services.interfaces.ChatRoomsService;
import ru.itis.services.interfaces.UsersService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class WebSocketMessagesHandler extends TextWebSocketHandler {
    private final Map<Long, List<WebSocketSession>> roomSessions = new HashMap<>();

    private final ChatMessagesService chatMessagesService;
    private final ChatRoomsService chatRoomsService;
    private final UsersService usersService;

    @Autowired
    private ObjectMapper objectMapper;

    public WebSocketMessagesHandler(ChatMessagesService chatMessagesService,
                                    ChatRoomsService chatRoomsService,
                                    UsersService usersService) {
        this.chatMessagesService = chatMessagesService;
        this.chatRoomsService = chatRoomsService;
        this.usersService = usersService;
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String messageText = (String) message.getPayload();
        ChatMessageFormDto messageFromJson = objectMapper.readValue(messageText, ChatMessageFormDto.class);

        Long roomId = messageFromJson.getRoomId();
        String text = messageFromJson.getText();
        Long userId = messageFromJson.getUserId();

        roomSessions.computeIfAbsent(roomId, k -> new ArrayList<>());
        if (!roomSessions.get(roomId).contains(session)) {
            roomSessions.get(roomId).add(session);
        }

        if (text.equals("")) {
        } else {
            UserDto userDto = usersService.findUser(userId);
            ChatRoomDto chatRoomDto = chatRoomsService.getRoom(roomId);

            ChatMessageDto savedMessage = chatMessagesService.save(
                    ChatMessageDto.builder()
                            .text(text)
                            .time(LocalDateTime.now())
                            .chatRoom(chatRoomDto)
                            .sender(userDto)
                            .build()
            );

            ChatMessageResponseDto chatMessageResponseDto = ChatMessageResponseDto
                    .builder()
                    .from(savedMessage.getSender().getEmail())
                    .text(savedMessage.getText())
                    .build();

            WebSocketMessage<String> responseMessage = new TextMessage(objectMapper.writeValueAsString(chatMessageResponseDto));

            for (WebSocketSession currentSession : roomSessions.get(roomId)) {
                currentSession.sendMessage(responseMessage);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        for (Map.Entry<Long, List<WebSocketSession>> entry : roomSessions.entrySet()) {
            entry.getValue().remove(session);
        }
    }
}
