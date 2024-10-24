package com.nameslowly.coinauctions.auction.domain.repository;

import com.nameslowly.coinauctions.auction.domain.model.Auction;
import com.nameslowly.coinauctions.auction.domain.model.AuctionStatus;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface AuctionRepository extends JpaRepository<Auction, Long> {

    List<Auction> findByAuctionStatus(AuctionStatus auctionStatus);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Auction a where a.id = :auctionId")
    Optional<Auction> findByIdWithLock(Long auctionId);
}
