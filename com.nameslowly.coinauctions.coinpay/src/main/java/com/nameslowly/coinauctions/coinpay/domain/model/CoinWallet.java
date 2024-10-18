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
@Table(name = "p_coin_wallets")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CoinWallet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coin_wallet_id")
    private Long id;
    @Column(nullable = false)
    private String username;
    @Column(name = "coin_id", nullable = false)
    private Long coinId;
    @Column(precision = 10, scale = 3, nullable = false)
    private BigDecimal quantity;

    public CoinWalletVO toCoinWalletVO() {
        return new CoinWalletVO(id, username, coinId, quantity);
    }

    public void coinWalletUpdate(BigDecimal quantity) {
        this.quantity = quantity;
    }
}
