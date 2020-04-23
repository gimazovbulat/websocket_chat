package ru.itis.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.dto.ChatRoomDto;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(schema = "chat", name = "chat_room")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Boolean active;
    private String name;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(schema = "chat", name = "chat_users", joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "chatter_id"))
    private List<User> chatters;
    @OneToMany(mappedBy = "chatRoom")
    @OrderBy("time")
    @Transient
    private List<ChatMessage> chatMessages;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ChatRoomType chatRoomType;
    @OneToOne
    private User creator;

    public static ChatRoomDto toChatRoomDto(ChatRoom chatRoom) {
        return ChatRoomDto.builder()
                .active(chatRoom.getActive())
                .chatRoomType(chatRoom.getChatRoomType())
                .name(chatRoom.getName())
                .id(chatRoom.getId())
                .creator(User.toUserDto(chatRoom.creator))
                .chatMessages(chatRoom.getChatMessages())
                .chatters(chatRoom.getChatters().stream().map(User::toUserDto).collect(Collectors.toList()))
                .build();
    }

    public static ChatRoom fromChatRoomDto(ChatRoomDto chatRoomDto) {
        return ChatRoom.builder()
                .active(chatRoomDto.getActive())
                .chatRoomType(chatRoomDto.getChatRoomType())
                .name(chatRoomDto.getName())
                .creator(User.fromUserDto(chatRoomDto.getCreator()))
                .id(chatRoomDto.getId())
                .chatMessages(chatRoomDto.getChatMessages())
                .chatters(chatRoomDto.getChatters().stream().map(User::fromUserDto).collect(Collectors.toList()))
                .build();
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "id=" + id +
                ", active=" + active +
                ", chatRoomType=" + chatRoomType +
                ", name='" + name + '\'' +
                '}';
    }
}
