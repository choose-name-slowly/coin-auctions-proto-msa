package com.nameslowly.coinauctions.auction.application.dto.request;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BidAuctionDto {

    private Long auctionId;
    private String winnerUsername;
    private BigDecimal winAmount;

}
