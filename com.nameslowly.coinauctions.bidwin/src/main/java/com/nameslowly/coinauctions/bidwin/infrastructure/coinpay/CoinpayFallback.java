package com.nameslowly.coinauctions.bidwin.infrastructure.coinpay;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CoinpayFallback implements CoinpayFeignClient {

    @Override
    public CoinDto getCoin() {
        log.info("coin service error");
//        throw new RuntimeException("user service error");
        return null;
    }
}
