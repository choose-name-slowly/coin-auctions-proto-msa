package com.nameslowly.coinauctions.coinpay.application.service;

import com.nameslowly.coinauctions.coinpay.domain.model.CoinHistory;
import com.nameslowly.coinauctions.coinpay.domain.model.CoinHistoryVO;
import com.nameslowly.coinauctions.coinpay.domain.repository.CoinHistoryRepository;
import com.nameslowly.coinauctions.common.exception.GlobalException;
import com.nameslowly.coinauctions.common.response.ResultCase;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CoinHistoryService {
    private final CoinHistoryRepository coinHistoryRepository;

    @Transactional(readOnly = true)
    public List<CoinHistoryVO> getCoinHistory() {
        List<CoinHistory> coinHistories = coinHistoryRepository.findAll();
        if(coinHistories.isEmpty()) {
            throw new GlobalException(ResultCase.NO_HISTORIES_FOUND);
        }
        return coinHistories.stream()
            .map(CoinHistory::toCoinHistoryVO)
            .collect(Collectors.toList());
    }
}
