package com.nameslowly.coinauctions.auction.domain.model;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.nameslowly.coinauctions.common.exception.GlobalException;
import com.nameslowly.coinauctions.common.response.ResultCase;
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

@Entity
@Table(name = "auctions")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Auction extends BaseEntity {

    private static final BigDecimal OPERATING_COASTS = new BigDecimal(500);
    private static final Integer AUCTION_DURATION_HOUR = 1;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "auction_id")
    private Long id;
    @Enumerated(EnumType.STRING)
    private AuctionStatus auctionStatus;

    private String title;
    private String image;
    private String description;

    @Column(precision = 10, scale = 3)
    private BigDecimal basePrice;
    private Long coinId;
    @Column(precision = 10, scale = 3)
    private BigDecimal fixedCoinPrice; // 경매 시작시 조회

    private String registerUsername;
    private LocalDateTime registerTime;

    private LocalDateTime startTime;
    private LocalDateTime endTime; // startTime + AUCTION_DURATION_HOUR

    private String winnerUsername; // 입찰시 갱신
    @Column(precision = 10, scale = 3)
    private BigDecimal winAmount; // 입찰시 갱신

    @Builder
    public Auction(String title, String image, String description, BigDecimal hopePrice,
        Long coinId, String registerUsername) {

        this.auctionStatus = AuctionStatus.PENDING;

        this.title = title;
        this.image = image;
        this.description = description;

        this.basePrice = hopePrice.add(OPERATING_COASTS);
        this.coinId = coinId;

        this.registerUsername = registerUsername;
        this.registerTime = LocalDateTime.now();
    }

    public void start(BigDecimal fixedCoinPrice) {

        if (this.auctionStatus != AuctionStatus.PENDING) {
            throw new GlobalException(ResultCase.NOT_PENDING_AUCTION);
        }

        this.auctionStatus = AuctionStatus.ONGOING;
        this.fixedCoinPrice = fixedCoinPrice;
        this.startTime = LocalDateTime.now();
        this.endTime = this.startTime.plusHours(AUCTION_DURATION_HOUR);
    }

    public void end() {

        if (this.auctionStatus != AuctionStatus.ONGOING) {
            throw new GlobalException(ResultCase.NOT_ONGOING_AUCTION);
        }

        if (this.endTime.isBefore(LocalDateTime.now())) {
            this.auctionStatus =
                this.winnerUsername == null ? AuctionStatus.FAIL : AuctionStatus.SUCCESS;
        }
    }

    public void updateWin(String winnerUsername, BigDecimal winAmount) {
        this.winnerUsername = winnerUsername;
        this.winAmount = winAmount;
    }
}
