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
    private String title;
    private String image;
    private String description;
    private AuctionStatus auctionStatus;
    private LocalDateTime registerTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal basePrice;
    private Long coinId;
    private BigDecimal fixedCoinPrice;
    private String registerMemberUsername;
    private BigDecimal currentAmount;

    public static AuctionDto of(Auction auction) {
        return AuctionDto.builder()
            .id(auction.getId())
            .title(auction.getTitle())
            .image(auction.getImage())
            .description(auction.getDescription())
            .auctionStatus(auction.getAuctionStatus())
            .registerTime(auction.getRegisterTime())
            .startTime(auction.getStartTime())
            .endTime(auction.getEndTime())
            .basePrice(auction.getBasePrice())
            .coinId(auction.getCoinId())
            .fixedCoinPrice(auction.getFixedCoinPrice())
            .registerMemberUsername(auction.getRegisterUsername())
            .currentAmount(auction.getWinAmount())
            .build();
    }

}
