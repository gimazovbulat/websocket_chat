package ru.itis.controllers;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.itis.dto.ChatRoomDto;
import ru.itis.dto.UniqueNameExceptionDto;
import ru.itis.dto.UserDto;
import ru.itis.models.ChatRoomType;
import ru.itis.security.JwtUtils;
import ru.itis.services.impl.ChatUsersComponent;
import ru.itis.services.interfaces.ChatRoomsService;
import ru.itis.services.interfaces.UsersService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class RoomsRestController {
    private final ChatRoomsService chatRoomsService;
    private final UsersService usersService;
    private final JwtUtils jwtUtils;
    private final ChatUsersComponent chatUsersComponent;

    public RoomsRestController(ChatRoomsService chatRoomsService,
                               UsersService usersService,
                               JwtUtils jwtUtils,
                               ChatUsersComponent chatUsersComponent) {
        this.chatRoomsService = chatRoomsService;
        this.usersService = usersService;
        this.jwtUtils = jwtUtils;
        this.chatUsersComponent = chatUsersComponent;
    }

    @PostMapping("/api/room")
    public ResponseEntity createRoom(
            @RequestParam("name") String name,
            HttpServletRequest servletRequest) {
        String email = jwtUtils.getSubject(servletRequest);
        UserDto user = usersService.findUser(email);
        List<UserDto> chatters = new ArrayList<>();
        chatters.add(user);

        try {
            chatRoomsService.createRoom(ChatRoomDto.builder()
                    .active(true)
                    .name(name)
                    .chatters(chatters)
                    .creator(user)
                    .chatRoomType(ChatRoomType.TO_USERS)
                    .build());
        }  catch (DataIntegrityViolationException e) {
            UniqueNameExceptionDto dto = UniqueNameExceptionDto.builder()
                    .entity("chat room")
                    .name(name)
                    .build();

            return ResponseEntity.of(Optional.of(dto));
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/rooms")
    public ResponseEntity<List<ChatRoomDto>> getAll(
            @RequestParam(value = "userId", required = false) Long userId) {
        List<ChatRoomDto> rooms;
        if (userId == null) {
            rooms = chatRoomsService.getAll();
        } else {
            UserDto user = usersService.findUser(userId);
            rooms = chatRoomsService.geAllRoomsForUser(user);
        }
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/api/rooms/{id}")
    public ResponseEntity<ChatRoomDto> getRoom(@PathVariable("id") Long roomId) {
        ChatRoomDto room = chatRoomsService.getRoom(roomId);

        return ResponseEntity.ok(room);
    }

    @PostMapping("/api/chat/users")
    public ResponseEntity addChatUser(@RequestParam("roomId") Long roomId,
                                      HttpServletRequest httpServletRequest) {

        String email = jwtUtils.getSubject(httpServletRequest);
        UserDto user = usersService.findUser(email);

        ChatRoomDto room = chatRoomsService.getRoom(roomId);
        if (!room.getChatters().contains(user)) {
            room.getChatters().add(user);
            chatRoomsService.update(room);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/chat/users")
    public ResponseEntity<List<UserDto>> getNewChatUsers(@RequestParam("roomId") Long roomId,
                                                         @RequestParam("pageId") String pageId) {
        List<UserDto> users = chatUsersComponent.get(roomId, pageId);
        return ResponseEntity.ok(users);
    }
}
