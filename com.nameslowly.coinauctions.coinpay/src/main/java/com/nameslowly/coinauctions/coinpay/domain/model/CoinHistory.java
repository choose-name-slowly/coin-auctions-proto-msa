package com.nameslowly.coinauctions.coinpay.domain.model;

import com.nameslowly.coinauctions.common.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@Builder
@Table(name = "p_coin_histories")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CoinHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="coin_history_id")
    private Long id;
    private String username;
    private Long coinId;
    @Column(precision = 10, scale = 3, nullable = false)
    private BigDecimal amount;
    @Column(precision = 10, scale = 3, name = "balance_before")
    private BigDecimal balanceBefore;
    @Column(precision = 10, scale = 3, name = "balance_after")
    private BigDecimal balanceAfter;
    private String reason; //EX)코인 충전, 코인 바인딩, 새로운 입찰로 인한 코인 회복 Enum으로 다루는게 나을라나?

    @Builder
    public CoinHistory(String username, Long coinId, BigDecimal amount, BigDecimal balanceBefore, BigDecimal balanceAfter, String reason) {
        this.username = username;
        this.coinId = coinId;
        this.amount = amount;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
        this.reason = reason;
    }
    public CoinHistoryVO toCoinHistoryVO() {return new CoinHistoryVO(id, username, coinId, amount, balanceBefore, balanceAfter, reason);}
}

