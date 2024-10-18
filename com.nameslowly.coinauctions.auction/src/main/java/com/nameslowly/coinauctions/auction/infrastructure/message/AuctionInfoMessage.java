package com.nameslowly.coinauctions.auction.infrastructure.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AuctionInfoMessage {
    private String title;
    private String endTime;
    private String registerMemberUsername;
}
