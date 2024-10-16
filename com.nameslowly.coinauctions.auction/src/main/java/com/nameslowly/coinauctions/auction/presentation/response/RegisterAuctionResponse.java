package com.nameslowly.coinauctions.auction.presentation.response;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class RegisterAuctionResponse implements Serializable {

    private Long auctionId;
}
