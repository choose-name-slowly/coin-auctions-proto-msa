package com.nameslowly.coinauctions.chat.presentation.controller;


import com.nameslowly.coinauctions.chat.application.dto.AuctionInfoMessage;
import com.nameslowly.coinauctions.chat.application.service.ChatroomService;
import com.nameslowly.coinauctions.chat.domain.model.Chatroom;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/api/chat")
public class ChatroomController {

    private final ChatroomService chatroomService;

    //채팅방 목록
    @GetMapping("/rooms-list")
    public String getChatroomList(@RequestHeader(value = "X-User-Name") String username, Model model) {
        getRoomList(username, model);
        return "rooms";
    }

    //채팅방 들어가기(상세)
    @GetMapping("/rooms/{chatroomId}")
    public String enterChatroom(@PathVariable String chatroomId, Model model, @RequestHeader(value = "X-User-Name") String username) {
        Chatroom chatroom = chatroomService.enterChatroom(chatroomId, username);
        model.addAttribute("room", chatroom);
        model.addAttribute("username", username);
        return "room-detail";
    }

    //채팅방 나가기 -> 채팅방을 나가도 채팅방을 삭제하지 않으면 과거 채팅 기록 조회 가능함
    @GetMapping("/rooms-out")
    public String exitChatroom(@RequestHeader(value = "X-User-Name") String username, Model model) {
        getRoomList(username, model);
        return "rooms";
    }

    //채팅방 생성
    @PostMapping("/rooms")
    public String createChatroom(@RequestParam("roomname") String roomname, @RequestHeader(value = "X-User-Name") String username, Model model){
        chatroomService.createChatroom(roomname, username);
        getRoomList(username, model);
        return "rooms";
    }

    //rabbitmq 메시징시스템에 의한 채팅방 생성
    @RabbitListener(queues = "${message.queue.auction-info}")
    public String receiveAuctionInfoMessage(AuctionInfoMessage auctionInfoMessage, @RequestHeader(value = "X-User-Name") String username, Model model){
        log.info("CHAT RECEIVE:{}", auctionInfoMessage.toString());
        chatroomService.createChatroom(auctionInfoMessage);
        getRoomList(username, model);
        return "rooms";
    }

    //채팅방 삭제 -> 채팅방 목록에서 보이지 않으므로 과거 채팅 기록 조회 불가능
    @PutMapping("/rooms/{chatroomId}")
    public String deleteChatroom(@PathVariable String chatroomId, @RequestHeader(value = "X-User-Name") String username, Model model){
        chatroomService.deleteChatMember(chatroomId, username);
        getRoomList(username, model);
        return "rooms";
    }

    public void getRoomList(String username, Model model){
        model.addAttribute("username", username);

        //모든 채팅방 목록
        List<Chatroom> chatroomList = chatroomService.getChatroomList();
        model.addAttribute("rooms", chatroomList);

        //내가 속한 채팅방 목록
        List<Chatroom> myChatroomList = chatroomService.getMyChatroomList(username);
        model.addAttribute("myrooms", myChatroomList);
    }
}
