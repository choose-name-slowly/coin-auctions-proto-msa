package com.nameslowly.coinauctions.auction.application.dto.response;

import com.nameslowly.coinauctions.auction.domain.model.Auction;
import com.nameslowly.coinauctions.auction.domain.model.AuctionStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuctionDto implements Serializable {

    private Long id;
    private AuctionStatus auctionStatus;

    private String title;
    private String image;
    private String description;

    private BigDecimal basePrice;
    private Long coinId;
    private BigDecimal fixedCoinPrice;

    private String registerUsername;
    private LocalDateTime registerTime;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private String winnerUsername;
    private BigDecimal winAmount;

    public static AuctionDto of(Auction auction) {
        return AuctionDto.builder()
            .id(auction.getId())
            .auctionStatus(auction.getAuctionStatus())
            .title(auction.getTitle())
            .image(auction.getImage())
            .description(auction.getDescription())
            .basePrice(auction.getBasePrice())
            .coinId(auction.getCoinId())
            .fixedCoinPrice(auction.getFixedCoinPrice())
            .registerUsername(auction.getRegisterUsername())
            .registerTime(auction.getRegisterTime())
            .startTime(auction.getStartTime())
            .endTime(auction.getEndTime())
            .winnerUsername(auction.getWinnerUsername())
            .winAmount(auction.getWinAmount())
            .build();
    }

}
