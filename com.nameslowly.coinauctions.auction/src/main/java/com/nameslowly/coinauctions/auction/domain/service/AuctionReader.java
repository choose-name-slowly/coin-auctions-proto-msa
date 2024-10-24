package com.nameslowly.coinauctions.auction.domain.service;

import com.nameslowly.coinauctions.auction.domain.model.Auction;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface AuctionReader {

    List<Auction> getPendingAuction();

    List<Auction> getOngoingAuction();

    List<Auction> getAuctionPage(Pageable page);

    Auction getAuction(Long auctionId);
    Auction getAuctionWithLock(Long auctionId);
}
