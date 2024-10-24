package com.nameslowly.coinauctions.auction.infrastructure;

import com.nameslowly.coinauctions.auction.domain.model.Auction;
import com.nameslowly.coinauctions.auction.domain.model.AuctionStatus;
import com.nameslowly.coinauctions.auction.domain.repository.AuctionRepository;
import com.nameslowly.coinauctions.auction.domain.service.AuctionReader;
import com.nameslowly.coinauctions.common.exception.GlobalException;
import com.nameslowly.coinauctions.common.response.ResultCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuctionReaderImpl implements AuctionReader {

    private final AuctionRepository auctionRepository;

    @Override
    public List<Auction> getPendingAuction() {
        return auctionRepository.findByAuctionStatus(AuctionStatus.PENDING);
    }

    @Override
    public List<Auction> getOngoingAuction() {
        return auctionRepository.findByAuctionStatus(AuctionStatus.ONGOING);
    }

    @Override
    public List<Auction> getAuctionPage(Pageable page) {
        return auctionRepository.findAll(page).getContent();
    }

    @Override
    public Auction getAuction(Long auctionId) {
        return auctionRepository.findById(auctionId).orElseThrow(
            () -> new GlobalException(ResultCase.NOT_FOUND_AUCTION)
        );
    }

    @Override
    public Auction getAuctionWithLock(Long auctionId) {
        return auctionRepository.findByIdWithLock(auctionId).orElseThrow(
            () -> new GlobalException(ResultCase.NOT_FOUND_AUCTION)
        );
    }
}
