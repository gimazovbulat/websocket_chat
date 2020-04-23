package ru.itis.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.dto.ChatMessageDto;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(schema = "chat", name = "chat_messages")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String text;
    @OneToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender;
    @ManyToOne
    @JoinColumn(name = "chat_room", referencedColumnName = "id")
    private ChatRoom chatRoom;
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

    public static ChatMessageDto toChatMessageDto(ChatMessage chatMessage){
        return ChatMessageDto.builder()
                .id(chatMessage.getId())
                .sender(User.toUserDto(chatMessage.getSender()))
                .chatRoom(ChatRoom.toChatRoomDto(chatMessage.getChatRoom()))
                .time(chatMessage.getTime())
                .text(chatMessage.getText())
                .build();
    }

    public static ChatMessage fromChatMessageDto(ChatMessageDto chatMessageDto){
        return ChatMessage.builder()
                .id(chatMessageDto.getId())
                .sender(User.fromUserDto(chatMessageDto.getSender()))
                .chatRoom(ChatRoom.fromChatRoomDto(chatMessageDto.getChatRoom()))
                .time(chatMessageDto.getTime())
                .text(chatMessageDto.getText())
                .build();
    }


}
