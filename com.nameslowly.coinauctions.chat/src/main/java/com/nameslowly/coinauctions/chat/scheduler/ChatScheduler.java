package com.nameslowly.coinauctions.chat.scheduler;


import com.nameslowly.coinauctions.chat.application.service.ChatroomService;
import com.nameslowly.coinauctions.chat.domain.model.Chatroom;
import com.nameslowly.coinauctions.chat.domain.repository.ChatMessageRepository;
import com.nameslowly.coinauctions.chat.domain.repository.ChatroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
@Component
public class ChatScheduler {
    private final ChatroomRepository chatroomRepository;
    private final ChatroomService chatroomService;
    private final ChatMessageRepository chatMessageRepository;

    //@Scheduled(fixedRate = 60000) // 1분마다 실행
    @Transactional
    public void closeChatroom() {
        List<Chatroom> chatroomList =  chatroomRepository.findAllByAuctionEndTimeIsNotNull();

        for(Chatroom chatroom : chatroomList){
            LocalDateTime auctionEndTime = LocalDateTime.parse(chatroom.getAuctionEndTime());

            if(auctionEndTime.isBefore(LocalDateTime.now())){ //auctionEndTime이 현재시간보다 이전시간이면
                chatroomService.deleteChatMemberAll(chatroom.getChatroomId());
                chatroom.setDelete(true);
            }
        }
    }

    //한달 이후의 채팅 메시지 일괄 삭제
    @Scheduled(cron = "0 0 0 1 * ?") //매월 1일 자정
    @Transactional
    public void deleteChatMessage() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beforeMonth = now.minusMonths(1);
        chatMessageRepository.deleteAllWithSendTimeOverMonth(beforeMonth);
    }
}