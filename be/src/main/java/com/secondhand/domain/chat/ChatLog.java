package com.secondhand.domain.chat;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class ChatLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String message;

    @Column(nullable = false, name = "read_count")
    private Integer readCount;

    @Column(nullable = false, name = "sender_id")
    private Long senderId;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @JoinColumn(nullable = false, name = "chat_room_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    @Builder
    private ChatLog(Long id, String message, Integer readCount, Long senderId, ChatRoom chatRoom) {
        this.id = id;
        this.message = message;
        this.readCount = readCount;
        this.senderId = senderId;
        this.chatRoom = chatRoom;
    }

    public static ChatLog of(ChatRoom chatRoom, String message, Long senderId) {
        return ChatLog.builder()
                .message(message)
                .readCount(1) //조회수
                .senderId(senderId)
                .chatRoom(chatRoom)
                .build();
    }
}
