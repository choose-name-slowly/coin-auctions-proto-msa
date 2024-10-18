package com.nameslowly.coinauctions.auction.application;

import com.nameslowly.coinauctions.auction.application.dto.request.BidAuctionDto;
import com.nameslowly.coinauctions.auction.application.dto.request.RegisterAuctionDto;
import com.nameslowly.coinauctions.auction.domain.model.Auction;
import com.nameslowly.coinauctions.auction.domain.service.AuctionReader;
import com.nameslowly.coinauctions.auction.domain.service.AuctionStore;
import com.nameslowly.coinauctions.auction.infrastructure.coinpay.CoinpayService;
import com.nameslowly.coinauctions.auction.infrastructure.message.AuctionInfoMessage;
import com.nameslowly.coinauctions.auction.infrastructure.user.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuctionService {

    @Value("${message.queue.auction-info}")
    private String auctionInfoQueue;

    private final AuctionStore auctionStore;
    private final AuctionReader auctionReader;
    private final CoinpayService coinpayService;
    private final UserService userService;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public Long registerAuction(RegisterAuctionDto dto) {
        userService.getUser(dto.getRegisterUsername());
        coinpayService.getCoin(dto.getCoinId());

        Auction auction = auctionStore.store(dto.toEntity());
        return auction.getId();
    }

//    @Scheduled(fixedRate = 1000L * 60 * 10)
    @Transactional
    public void startAuction() {
        auctionReader.getPendingAuction().forEach(
            auction -> {
                auction.start(coinpayService.getCoin(auction.getCoinId()).getCoin_price());
                rabbitTemplate.convertAndSend(auctionInfoQueue, new AuctionInfoMessage(auction.getTitle(),
                    auction.getEndTime().toString(), auction.getRegisterUsername()));
            }
        );
    }

//    @Scheduled(fixedRate = 1000)
    @Transactional
    public void endAuction() {
        auctionReader.getOngoingAuction().forEach(Auction::end);
    }

    @Transactional
    public void updateAuctionWin(BidAuctionDto dto) {
        Auction auction = auctionReader.getAuction(dto.getAuctionId());
        auction.updateWin(dto.getWinnerUsername(), dto.getWinAmount());
    }

    @Transactional(readOnly = true)
    public List<Auction> retrieveAuctionPage(Pageable page) {
        List<Auction> auctionPage = auctionReader.getAuctionPage(page);
        return auctionPage;
    }

    @Transactional(readOnly = true)
    public Auction retrieveAuction(Long auctionId) {
        return auctionReader.getAuction(auctionId);
    }
}
