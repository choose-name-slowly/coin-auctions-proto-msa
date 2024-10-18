package com.nameslowly.coinauctions.coinpay.application.service;

import com.nameslowly.coinauctions.coinpay.application.dto.response.BidRefundMessage;
import com.nameslowly.coinauctions.coinpay.application.dto.response.CoinHistoryMessage;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoinPayListener {
    private final CoinWalletService coinWalletService;
    private final CoinHistoryService coinHistoryService;

//    @RabbitListener(queues = "${message.queue.bid-cancel}")
    @RabbitListener(queues = "app.bid.cancel")
    public void handleBidRefund(BidRefundMessage bidRefundMessage) {
        // 메시지에 담긴 정보로 유저의 코인 복구
        coinWalletService.restoreCoins(bidRefundMessage.getUsername(), bidRefundMessage.getCoin_id(), bidRefundMessage.getAmount());
    }
    @RabbitListener(queues = "app.coinpay.history")
    public void handleCoinHistory(CoinHistoryMessage coinHistoryMessage){
        coinHistoryService.createCoinHistory(coinHistoryMessage.getUsername(), coinHistoryMessage.getCoinId(), coinHistoryMessage.getAmount(),
            coinHistoryMessage.getBalanceBefore(), coinHistoryMessage.getBalanceAfter(), coinHistoryMessage.getReason());
    }
}
