package com.nameslowly.coinauctions.chat.domain.model;

import com.nameslowly.coinauctions.chat.application.dto.ChatMessageDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;


@SQLRestriction("is_delete = false")
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "chat_messages")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_id")
    private Long chatMessageId;

    @Column(nullable = false)
    private String senderId; //username에 해당

    //@Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String message;

    private LocalDateTime sendTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MessageType type;

    private String imageurl;

    private boolean isDelete = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id", nullable = false)
    private Chatroom chatroom;

    public ChatMessage(ChatMessageDto chatMessageDto, Chatroom chatroom){
        this.senderId = chatMessageDto.getSenderId();
        this.message = chatMessageDto.getMessage();
        this.sendTime = LocalDateTime.now();
        this.type = MessageType.valueOf(chatMessageDto.getType());
        this.imageurl = chatMessageDto.getImageurl();
        this.chatroom = chatroom;
    }

    public ChatMessage(ChatMessageDto chatMessageDto, String enterMsg, Chatroom chatroom){
        this.senderId = chatMessageDto.getSenderId();
        this.message = enterMsg;
        this.sendTime = LocalDateTime.now();
        this.type = MessageType.valueOf(chatMessageDto.getType());
        this.imageurl = chatMessageDto.getImageurl();
        this.chatroom = chatroom;
    }
}
