package com.nameslowly.coinauctions.bidwin.domain.model;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.nameslowly.coinauctions.common.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Entity
@Table(name = "bids")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Bid extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "bid_id")
    private Long id;
    @Enumerated(EnumType.STRING)
    private BidStatus bidStatus;

    private String bidderUsername;
    private Long auctionId;
    private Long coinId;
    @Column(precision = 10, scale = 3)
    private BigDecimal bidAmount;
    private LocalDateTime bidTime;

    @Builder
    public Bid(Long auctionId, String bidderUsername, Long coinId,
        BigDecimal bidAmount) {
        this.auctionId = auctionId;
        this.bidderUsername = bidderUsername;
        this.coinId = coinId;
        this.bidAmount = bidAmount;
        this.bidStatus = BidStatus.WIN;
        this.bidTime = LocalDateTime.now();
    }

    public void cancel() {
        this.bidStatus = BidStatus.CANCEL;
    }
}
