package com.nameslowly.coinauctions.bidwin.infrastructure.service;

import com.nameslowly.coinauctions.bidwin.domain.model.Bid;
import com.nameslowly.coinauctions.bidwin.domain.repository.BidRepository;
import com.nameslowly.coinauctions.bidwin.domain.service.BidStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BidStoreImpl implements BidStore {

    private final BidRepository bidRepository;

    @Override
    public Bid store(Bid initBid) {
        return bidRepository.save(initBid);
    }

}
