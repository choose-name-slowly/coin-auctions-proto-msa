package com.nameslowly.coinauctions.auction.infrastructure.message;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BidRegisterMessage {

    private Long auctionId;
    private BigDecimal bidAmount;

}
