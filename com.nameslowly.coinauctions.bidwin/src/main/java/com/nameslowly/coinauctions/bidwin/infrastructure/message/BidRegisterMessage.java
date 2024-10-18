package com.nameslowly.coinauctions.bidwin.infrastructure.message;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BidRegisterMessage {

    private Long auctionId;
    private String winnerUsername;
    private BigDecimal winAmount;

}
