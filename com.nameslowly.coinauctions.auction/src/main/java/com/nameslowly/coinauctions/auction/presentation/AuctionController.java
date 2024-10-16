package com.nameslowly.coinauctions.auction.presentation;

import com.nameslowly.coinauctions.auction.application.AuctionService;
import com.nameslowly.coinauctions.auction.domain.model.Auction;
import com.nameslowly.coinauctions.auction.infrastructure.coinpay.CoinpayService;
import com.nameslowly.coinauctions.auction.infrastructure.message.BidRegisterMessage;
import com.nameslowly.coinauctions.auction.presentation.request.RegisterAuctionRequest;
import com.nameslowly.coinauctions.auction.application.dto.response.AuctionDto;
import com.nameslowly.coinauctions.auction.presentation.request.UpdateAuctionWinRequest;
import com.nameslowly.coinauctions.auction.presentation.response.RegisterAuctionResponse;
import com.nameslowly.coinauctions.auction.presentation.response.RetrieveAuctionPageResponse;
import com.nameslowly.coinauctions.auction.presentation.response.RetrieveAuctionResponse;
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
    public CommonResponse<RegisterAuctionResponse> registerAuction(
        @RequestBody RegisterAuctionRequest request) {
        Long auctionId = auctionService.registerAuction(request.toDto());
        return CommonResponse.success(new RegisterAuctionResponse(auctionId));
    }

    @GetMapping("/api/auctions")
    public CommonResponse<List<RetrieveAuctionPageResponse>> retrieveAuctionPage(Pageable page) {
        List<Auction> auctionPage = auctionService.retrieveAuctionPage(page);
        List<RetrieveAuctionPageResponse> response = auctionPage.stream()
            .map(RetrieveAuctionPageResponse::of).toList();
        return CommonResponse.success(response);
    }

    @PostMapping("/api/auctions/start")
    public CommonResponse startAuction() {
        auctionService.startAuction();
        return CommonResponse.success();
    }

    @PostMapping("/api/auctions/{auctionId}/updateWin")
    public CommonResponse updateAuctionWin(UpdateAuctionWinRequest request) {
        return CommonResponse.success();
    }

    @PostMapping("/api/auctions/end")
    public CommonResponse endAuction() {
        auctionService.endAuction();
        return CommonResponse.success();
    }

    @GetMapping("/api/auctions/{auctionId}")
    public CommonResponse<RetrieveAuctionResponse> retrieveAuction(@PathVariable("auctionId") Long auctionId) {
        Auction auction = auctionService.retrieveAuction(auctionId);
        RetrieveAuctionResponse response = RetrieveAuctionResponse.of(auction);
        return CommonResponse.success(response);
    }

    // internal

    @GetMapping("/api/internal/auctions/{auctionId}")
    public AuctionDto getAuction(@PathVariable("auctionId") Long auctionId) {
        Auction auction = auctionService.retrieveAuction(auctionId);
        return AuctionDto.of(auction);
    }

    // message

    @RabbitListener(queues = "${message.queue.bid-register}")
    public void bidAuction(BidRegisterMessage message) {
        auctionService.updateAuctionWin(message.toDto());
    }
}
