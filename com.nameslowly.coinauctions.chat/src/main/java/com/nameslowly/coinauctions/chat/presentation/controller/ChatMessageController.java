package com.nameslowly.coinauctions.chat.presentation.controller;

import com.nameslowly.coinauctions.chat.application.dto.ChatMessageDto;
import com.nameslowly.coinauctions.chat.application.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatMessageController {

    private final SimpMessagingTemplate template;
    private final ChatMessageService chatMessageService;

    @Autowired
    public ChatMessageController(SimpMessagingTemplate template, ChatMessageService chatMessageService) {
        this.template = template;
        this.chatMessageService = chatMessageService;
    }


    @MessageMapping("/chat/enter")
    public void enter(ChatMessageDto message){
        //채팅방 과거 메시지 기록 조회
        String beforeMsgs = chatMessageService.getChatMessages(message);

        String enterMsg = "--- " + message.getSenderId() + "님이 입장하셨습니다. ---";
        message.setMessage(beforeMsgs + enterMsg);

        chatMessageService.saveChatMessage(message, enterMsg);
        template.convertAndSend("/subscribe/chat/room/inout/" + message.getChatroomId(), message);

    }

    @MessageMapping("/chat/talk")
    public void talk(ChatMessageDto message) {
        chatMessageService.saveChatMessage(message);
        template.convertAndSend("/subscribe/chat/room/" + message.getChatroomId(), message);
    }

    @MessageMapping("/chat/exit")
    public void exit(ChatMessageDto message) {
        message.setMessage("--- " + message.getSenderId() + "님이 퇴장하셨습니다. ---");
        chatMessageService.saveChatMessage(message);
        template.convertAndSend("/subscribe/chat/room/inout/" + message.getChatroomId(), message);
    }
}
