package com.nameslowly.coinauctions.bidwin.presentation.response;

import com.nameslowly.coinauctions.bidwin.domain.model.Bid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RetrieveBidResponse {

    private Long id;
    private Long auctionId;
    private String participantMemberUsername;
    private Long coinId;
    private BigDecimal coinAmount;
    private LocalDateTime bidTime;

    public static RetrieveBidResponse of(Bid bid) {
        return RetrieveBidResponse.builder()
            .id(bid.getId())
            .auctionId(bid.getAuctionId())
            .participantMemberUsername(bid.getBidderUsername())
            .coinId(bid.getCoinId())
            .coinAmount(bid.getBidAmount())
            .bidTime(bid.getBidTime())
            .build();
    }

}
