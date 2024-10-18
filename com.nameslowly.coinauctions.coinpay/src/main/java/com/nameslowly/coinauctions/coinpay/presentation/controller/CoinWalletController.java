package com.nameslowly.coinauctions.coinpay.presentation.controller;

import com.nameslowly.coinauctions.coinpay.domain.model.CoinWalletVO;
import com.nameslowly.coinauctions.coinpay.application.dto.request.CoinBidRequest;
import com.nameslowly.coinauctions.coinpay.application.dto.request.CoinChargeRequest;
import com.nameslowly.coinauctions.coinpay.application.service.CoinWalletService;
import com.nameslowly.coinauctions.common.response.CommonResponse;
import com.nameslowly.coinauctions.common.shared.RoleCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CoinWalletController {
    private final CoinWalletService coinWalletService;

    @RoleCheck(roles = {"USER"})
    @PostMapping("/coin_wallets")
    public CommonResponse chargeCoin(@RequestBody CoinChargeRequest request, @RequestHeader("X-User-Name") String username){
        CoinWalletVO coinWallet = coinWalletService.saveCoinWallet(request, username);
        return CommonResponse.success(coinWallet);
    }
    @PutMapping("/internal/coin_wallets/use") //해당 api는 feign요청이므로 일단은 CommonResponse를 따로 통일 안함 테스트 우선 감소
    public boolean changeBidCoin(@RequestBody CoinBidRequest request){
        return coinWalletService.changeBidCoin(request);
    }
    @PutMapping("/internal/coin_wallets/recover")
    public void recoverBidCoin(@RequestBody CoinBidRequest request){
         coinWalletService.recoverBidCoin(request);
    }
    @RoleCheck(roles = {"USER"})
    @GetMapping("/coin_wallets")
    public CommonResponse getCoinWallet(@RequestHeader("X-User-Name") String username){
        List<CoinWalletVO> coinWallet = coinWalletService.getCoinWallet(username);
        return CommonResponse.success(coinWallet);
    }

}
