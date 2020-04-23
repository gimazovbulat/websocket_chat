package ru.itis.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.itis.dto.ChatRoomDto;
import ru.itis.dto.UserDto;
import ru.itis.security.JwtUtils;
import ru.itis.services.impl.ChatUsersComponent;
import ru.itis.services.interfaces.ChatRoomsService;
import ru.itis.services.interfaces.UsersService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Controller
public class ChatRoomController {
    private final ChatRoomsService chatRoomsService;
    private final JwtUtils jwtUtils;
    private final UsersService usersService;
    private final ChatUsersComponent chatUsersComponent;

    public ChatRoomController(ChatRoomsService chatRoomsService,
                              JwtUtils jwtUtils,
                              UsersService usersService,
                              ChatUsersComponent chatUsersComponent) {
        this.chatRoomsService = chatRoomsService;
        this.jwtUtils = jwtUtils;
        this.usersService = usersService;
        this.chatUsersComponent = chatUsersComponent;
    }

    @GetMapping("/rooms")
    public String getAllRoomsPage(Model model, HttpServletRequest servletRequest) {
        String email = jwtUtils.getSubject(servletRequest);
        UserDto user = usersService.findUser(email);

        model.addAttribute("user", user);

        List<ChatRoomDto> allRooms = chatRoomsService.getAll();
        model.addAttribute("rooms", allRooms);

        return "rooms";
    }

    @GetMapping("/rooms/{id}")
    public String getSingleRoomPage(@PathVariable("id") Long roomId, Model model, HttpServletRequest servletRequest) {
        ChatRoomDto room = chatRoomsService.getRoom(roomId);
        String email = jwtUtils.getSubject(servletRequest);
        UserDto user = usersService.findUser(email);

        if (!room.getChatters().contains(user)){
            room.getChatters().add(user);
            chatUsersComponent.add(user, roomId);
            chatRoomsService.update(room);
        }

        model.addAttribute("room", room);
        model.addAttribute("user", user);
        model.addAttribute("pageId", UUID.randomUUID());

        return "chat";
    }
}
