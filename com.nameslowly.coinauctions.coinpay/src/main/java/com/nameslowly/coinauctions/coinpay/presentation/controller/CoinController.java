package com.nameslowly.coinauctions.coinpay.presentation.controller;

import com.nameslowly.coinauctions.coinpay.domain.model.CoinVO;
import com.nameslowly.coinauctions.coinpay.application.dto.request.CoinCreateRequest;
import com.nameslowly.coinauctions.coinpay.application.service.CoinService;
import com.nameslowly.coinauctions.common.response.CommonResponse;
import com.nameslowly.coinauctions.common.shared.RoleCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CoinController {

    private final CoinService coinService;

    @RoleCheck(roles = {"MASTER"})
    @PostMapping("/coins/create")
    public CommonResponse createCoin(@RequestBody CoinCreateRequest coinRequest) {
        // Upbit API에서 코인 가격 가져오기
        BigDecimal coinPrice = coinService.getCoinPriceFromUpbit(coinRequest.getCoin_real_name());
        // 코인 정보 저장
        CoinVO coin = coinService.saveCoin(coinRequest.getCoin_name(),coinRequest.getCoin_real_name(), coinPrice);
        return CommonResponse.success(coin);
    }

    @GetMapping("/coins")
    public CommonResponse getAllCoins() {
        return CommonResponse.success(coinService.getAllCoins());
    }

    @RoleCheck(roles = {"MASTER"})
    @DeleteMapping("/coins/{coinId}")
    public CommonResponse deleteCoin(@PathVariable Long coinId){
        CoinVO coin = coinService.deleteCoin(coinId);
        return CommonResponse.success(coin);
    }
    @GetMapping("/internal/coins/{coinId}")
    public CoinVO getCoinById(@PathVariable("coinId") Long coinId) {
        return coinService.getCoinById(coinId);
    }
}