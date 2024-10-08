package com.nameslowly.coinauctions.auction.presentation;

import com.nameslowly.coinauctions.auction.application.AuctionService;
import com.nameslowly.coinauctions.auction.domain.model.Auction;
import com.nameslowly.coinauctions.auction.infrastructure.coinpay.CoinpayService;
import com.nameslowly.coinauctions.auction.infrastructure.message.BidRegisterMessage;
import com.nameslowly.coinauctions.auction.presentation.request.RegisterAuctionRequest;
import com.nameslowly.coinauctions.auction.presentation.response.AuctionDto;
import com.nameslowly.coinauctions.auction.presentation.response.RegisterAuctionResponse;
import com.nameslowly.coinauctions.common.response.CommonResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;
    private final CoinpayService coinpayService;

    @PostMapping("/api/auctions")
    public CommonResponse<RegisterAuctionResponse> register(
        @RequestBody RegisterAuctionRequest request) {
        Long auctionId = auctionService.register(request.toDto());
        return CommonResponse.success(new RegisterAuctionResponse(auctionId));
    }

    @GetMapping("/api/auctions")
    public CommonResponse<List<Auction>> retrieveAuctionPage(Pageable page) {
        List<Auction> auctionPage = auctionService.retrieveAuctionPage(page);
        return CommonResponse.success(auctionPage);
    }

    @GetMapping("/api/auctions/{auctionId}")
    public CommonResponse<Auction> retrieveAuction(@PathVariable("auctionId") Long auctionId) {
        Auction auction = auctionService.retrieveAuction(auctionId);
        return CommonResponse.success(auction);
    }

    @GetMapping("/api/internal/auctions/{auctionId}")
    public AuctionDto getAuction(@PathVariable("auctionId") Long auctionId) {
        Auction auction = auctionService.retrieveAuction(auctionId);
        return AuctionDto.of(auction);
    }

    @RabbitListener(queues = "${message.queue.bid-register}") // todo
    public void updateCurrentAmount(BidRegisterMessage message) {
        auctionService.updateCurrentAmount(message.getAuctionId(), message.getBidAmount());
    }
}
