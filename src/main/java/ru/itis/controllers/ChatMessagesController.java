package ru.itis.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.dto.ChatMessageDto;
import ru.itis.services.interfaces.ChatMessagesService;

import java.util.List;

@RestController
public class ChatMessagesController {
    private final ChatMessagesService chatMessagesService;

    public ChatMessagesController(ChatMessagesService chatMessagesService) {
        this.chatMessagesService = chatMessagesService;
    }

    @GetMapping("/messages")
    public ResponseEntity<List<ChatMessageDto>> getMessages(@RequestParam("roomId") Long roomId) {
        List<ChatMessageDto> messages = chatMessagesService.findAll(roomId);
        return ResponseEntity.ok(messages);
    }
}
