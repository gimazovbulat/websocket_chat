package ru.itis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ChatMessageDto {
    private Long id;
    private String text;
    private UserDto sender;
    private ChatRoomDto chatRoom;
    private LocalDateTime time;

    @Override
    public String toString() {
        return "ChatMessage{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", sender=" + sender.getEmail() +
                ", time=" + time +
                '}';
    }
}

