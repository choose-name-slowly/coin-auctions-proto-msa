package com.nameslowly.coinauctions.bidwin.domain.service;

import com.nameslowly.coinauctions.bidwin.domain.model.Bid;

public interface BidStore {

    Bid store(Bid initBid);
}
