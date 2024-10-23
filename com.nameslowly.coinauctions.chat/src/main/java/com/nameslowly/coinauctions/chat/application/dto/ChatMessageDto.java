package com.nameslowly.coinauctions.chat.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageDto {
    private Long chatMessageId;
    private String senderId; //username
    private String nickname;
    private String message;
    private LocalDateTime sendTime;
    private String type;
    private String imageurl;
    private boolean isDelete;
    private Long chatroomId;
}
