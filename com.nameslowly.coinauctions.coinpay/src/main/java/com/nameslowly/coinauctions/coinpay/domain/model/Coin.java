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
@Table(name = "p_coins")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coin_id")
    private Long id;

    @Column(name = "coin_name", nullable = false, unique = true)
    private String coinName;
    @Column(name = "coin_real_name", nullable = false, unique = true)
    private String coinRealName;
    @Column(precision = 10, scale = 3, name = "coin_price", nullable = false)
    private BigDecimal coinPrice;
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    // Getters and Setters
    @Builder
    public Coin(String coinName, String coinRealName, BigDecimal coinPrice) {
        this.coinName = coinName;
        this.coinRealName = coinRealName;
        this.coinPrice = coinPrice;
        this.isDeleted = false;
    }

    public void coinUpdate(BigDecimal price) {
        this.coinPrice = price;
    }

    public void coinDelete() {
        this.isDeleted = true;
    }

    public CoinVO toCoinVO() {
        return new CoinVO(id, coinName, coinRealName, coinPrice);
    }
}