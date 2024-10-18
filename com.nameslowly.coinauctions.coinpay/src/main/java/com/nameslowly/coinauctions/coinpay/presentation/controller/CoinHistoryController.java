package com.nameslowly.coinauctions.coinpay.presentation.controller;

import com.nameslowly.coinauctions.coinpay.application.service.CoinHistoryService;
import com.nameslowly.coinauctions.coinpay.domain.model.CoinHistory;
import com.nameslowly.coinauctions.coinpay.domain.model.CoinHistoryVO;
import com.nameslowly.coinauctions.coinpay.domain.repository.CoinHistoryRepository;
import com.nameslowly.coinauctions.common.response.CommonResponse;
import com.nameslowly.coinauctions.common.shared.RoleCheck;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coin_histories")
public class CoinHistoryController {

    private final CoinHistoryService coinHistoryService;

    @RoleCheck(roles = {"MASTER"})
    @GetMapping
    public CommonResponse getCoinHistory() {
        return CommonResponse.success(coinHistoryService.getCoinHistory());
    }
}
