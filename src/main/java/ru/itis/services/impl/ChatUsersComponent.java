package ru.itis.services.impl;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import ru.itis.dto.UserDto;

import java.util.*;

@Component
public class ChatUsersComponent {
    private final Map<String, List<UserDto>> users; //page - users
    private final Map<Long, Set<String>> openRooms; //room - pages

    public ChatUsersComponent() {
        users = new HashMap<>();
        openRooms = new HashMap<>();
    }

    public void add(UserDto user, Long roomId) {
        openRooms.computeIfAbsent(roomId, k -> new HashSet<>());
        Set<String> pages = openRooms.get(roomId);
        for (String page : pages) {
            if (!users.get(page).contains(user)) {
                synchronized (users.get(page)) {
                    users.get(page).add(user);
                    users.get(page).notifyAll();
                }
            }
        }
    }

    @SneakyThrows
    public List<UserDto> get(Long roomId, String pageId) {
        openRooms.computeIfAbsent(roomId, k -> new HashSet<>());
        openRooms.get(roomId).add(pageId);
        if (!users.containsKey(pageId)) {
            users.put(pageId, new ArrayList<>());
        }
        synchronized (users.get(pageId)) {
            if (users.get(pageId).isEmpty()) {
                users.get(pageId).wait();
            }
        }
        List<UserDto> response = new ArrayList<>(users.get(pageId));
        users.get(pageId).clear();
        return response;
    }
}
