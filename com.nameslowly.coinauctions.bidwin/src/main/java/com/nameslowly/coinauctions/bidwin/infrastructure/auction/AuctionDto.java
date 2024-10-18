package com.nameslowly.coinauctions.bidwin.infrastructure.auction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuctionDto {

    private Long id;
    private String auctionStatus;

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

}
