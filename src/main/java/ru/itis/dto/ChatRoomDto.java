package ru.itis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.models.ChatMessage;
import ru.itis.models.ChatRoomType;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomDto {
    private Long id;
    private Boolean active;
    private ChatRoomType chatRoomType;
    private String name;
    private List<UserDto> chatters;
    private List<ChatMessage> chatMessages;
    private UserDto creator;

    @Override
    public String toString() {
        return "ChatRoomDto{" +
                "id=" + id +
                ", active=" + active +
                ", chatRoomType=" + chatRoomType +
                ", name='" + name + '\'' +
                '}';
    }
}
