package com.nameslowly.coinauctions.chat.application.service;


import com.nameslowly.coinauctions.chat.application.dto.AuctionInfoMessage;
import com.nameslowly.coinauctions.chat.domain.model.Chatroom;
import com.nameslowly.coinauctions.chat.domain.model.ChatroomMember;
import com.nameslowly.coinauctions.chat.domain.repository.ChatroomMemberRepository;
import com.nameslowly.coinauctions.chat.domain.repository.ChatroomRepository;
import com.nameslowly.coinauctions.common.exception.GlobalException;
import com.nameslowly.coinauctions.common.response.ResultCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ChatroomService {
    private final ChatroomRepository chatroomRepository;
    private final ChatroomMemberRepository chatroomMemberRepository;

    public List<Chatroom> getChatroomList() {
        return chatroomRepository.findAll();
    }


    public List<Chatroom> getMyChatroomList(String username) {
        List<ChatroomMember> chatroomMemberList = chatroomMemberRepository.findAllByUsernameAndIsDeleteFalse(username);
        List<Chatroom> chatroomList = new ArrayList<>();
        for (ChatroomMember chatroomMember : chatroomMemberList) {
            Chatroom chatroom = chatroomRepository.findById(chatroomMember.getChatroom().getChatroomId()).orElseThrow(
                    () -> new GlobalException(ResultCase.CHATROOM_NOT_FOUND)
            );
            chatroomList.add(chatroom);
        }
        return chatroomList;
    }


    @Transactional
    public Chatroom enterChatroom(String chatroomId, String username) {
        Chatroom chatroom =  chatroomRepository.findById(Long.valueOf(chatroomId)).orElseThrow(
                () -> new GlobalException(ResultCase.CHATROOM_NOT_FOUND)
        );

        //채팅룸 입장시 chatMember가 있다면 삭제상태를 false로 바꾸고, chatMember에 없다면 추가
        Optional<ChatroomMember> chatroomMember = chatroomMemberRepository.findByUsernameAndChatroom(username, chatroom);
        if(chatroomMember.isPresent()){
            if(chatroomMember.get().isDelete() == true){
                chatroomMember.get().setDelete(false);
            }
        }else{
            chatroomMemberRepository.save(new ChatroomMember(chatroom, username));
        }

        return chatroom;
    }

    @Transactional
    public void createChatroom(String roomname, String username) {
        Chatroom chatroom = chatroomRepository.save(new Chatroom(roomname));
        chatroomMemberRepository.save(new ChatroomMember(chatroom, username));
    }

    @Transactional
    public void createChatroom(AuctionInfoMessage auctionInfoMessage) {
        String roomname = auctionInfoMessage.getTitle() + " 채팅방";
        String auctionEndTime = auctionInfoMessage.getEndTime();
        String username = auctionInfoMessage.getRegisterMemberUsername();

        Chatroom chatroom = chatroomRepository.save(new Chatroom(roomname, auctionEndTime));
        chatroomMemberRepository.save(new ChatroomMember(chatroom, username));
    }

    @Transactional
    public void deleteChatMember(String chatroomId, String username) {
        Chatroom chatroom = chatroomRepository.findById(Long.valueOf(chatroomId)).orElseThrow(
                () -> new GlobalException(ResultCase.CHATROOM_NOT_FOUND)
        );

        ChatroomMember chatroomMember = chatroomMemberRepository.findByChatroomAndUsername(chatroom, username);
        chatroomMember.setDelete(true);
    }

    @Transactional
    public void deleteChatMemberAll(Long chatroomId) {
        Chatroom chatroom = chatroomRepository.findById(Long.valueOf(chatroomId)).orElseThrow(
                () -> new RuntimeException("채팅방이 존재하지 않습니다.")
        );

        List<ChatroomMember> chatroomMemberList = chatroomMemberRepository.findAllByChatroom(chatroom);
        for(ChatroomMember chatroomMember : chatroomMemberList){
            chatroomMember.setDelete(true);
        }
    }
}
