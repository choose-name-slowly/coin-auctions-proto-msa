package com.nameslowly.coinauctions.bidwin.application;

import com.nameslowly.coinauctions.bidwin.application.dto.RegisterBidDto;
import com.nameslowly.coinauctions.bidwin.domain.model.Bid;
import com.nameslowly.coinauctions.bidwin.domain.service.BidFactory;
import com.nameslowly.coinauctions.bidwin.domain.service.BidReader;
import com.nameslowly.coinauctions.bidwin.infrastructure.auction.AuctionDto;
import com.nameslowly.coinauctions.bidwin.infrastructure.auction.AuctionService;
import com.nameslowly.coinauctions.bidwin.infrastructure.coinpay.CoinBidRequest;
import com.nameslowly.coinauctions.bidwin.infrastructure.coinpay.CoinpayService;
import com.nameslowly.coinauctions.bidwin.infrastructure.message.BidCancelMessage;
import com.nameslowly.coinauctions.bidwin.infrastructure.message.BidRegisterMessage;
import com.nameslowly.coinauctions.bidwin.infrastructure.user.UserService;
import com.nameslowly.coinauctions.common.exception.GlobalException;
import com.nameslowly.coinauctions.common.response.ResultCase;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BidService {

    private final BidReader bidReader;
    private final BidFactory bidFactory;

    private final UserService userService;
    private final CoinpayService coinpayService;
    private final AuctionService auctionService;

    private final RabbitTemplate rabbitTemplate;
    @Value("${message.queue.bid-cancel}")
    private String queueBidCancel;
    @Value("${message.queue.bid-register}")
    private String queueBidRegister;

    /***
     * 1. participant member exist // not -> exception
     * 2. auction exist // not -> exception
     *      2.1 auction status ongoing check
     * 3. coin exist // not -> exception
     *      4.1 win bid exist
     *          5.1 win bid coin amount < new bid coin amount
     *              6. new bider coin decrease // after if error -> exception, new bider coin increase (rollback)
     *                   7. create new bid // exception -> new bider coin increase
     *                      8.1 cancel win bid (event)
     *                          9.1 win bid status change (WIN -> CANCEL)
     *                          9.2 win bider coin increase and member coin update (event)
     *                              10. win bider coin history record // if error -> retry
     *                      8.2 chatting create (event)
     *          5.2 win bid coin amount > new bid coin amount -> exception
     *      4.2 not
     *          5.1 auction base coin amount < new bid coin amount
     *              6. create new bid
     *              7. chatting create (event)
     *          5.2 auction base coin amount > new bid coin amount -> exception
     */
    @Transactional
    public Long register(RegisterBidDto dto) {
        userService.getUser(dto.getBidderUsername());
        coinpayService.getCoin(dto.getCoinId());
        AuctionDto auction = auctionService.getAuction(dto.getAuctionId());

        if (!auction.getAuctionStatus().equals("ONGOING")) {
            throw new GlobalException(ResultCase.NOT_ONGOING_AUCTION);
        }

        Boolean isNewBid = false;
        BidCancelMessage bidCancelMessage = null;
        Optional<Bid> winBidOpt = bidReader.findWinBidByAuctionId(dto.getAuctionId());
        if (winBidOpt.isPresent()) {
            Bid winBid = winBidOpt.get();

            if (auction.getRegisterUsername().equals(dto.getBidderUsername())) {
                throw new GlobalException(ResultCase.AUCTION_REGISTER);
            }

            if (winBid.getBidderUsername().equals(dto.getBidderUsername())) {
                throw new GlobalException(ResultCase.CURRENT_WINNER);
            }

            if (notExceed(dto.getBidAmount(), winBid.getBidAmount())) {
                throw new GlobalException(ResultCase.NOT_ENOUGH_THAN_CURRENT_PRICE);
            }

            winBid.cancel();

            bidCancelMessage =new BidCancelMessage(winBid.getBidderUsername(), winBid.getCoinId(), winBid.getBidAmount());
        } else {
            isNewBid = true;

            if (notExceed(dto.getBidAmount().multiply(auction.getFixedCoinPrice()), auction.getBasePrice())) {
                throw new GlobalException(ResultCase.NOT_ENOUGH_THAN_BASE_AMOUNT);
            }
        }

        if (!coinpayService.useCoin(new CoinBidRequest(dto.getBidderUsername(), dto.getCoinId(), dto.getBidAmount()))) {
            throw new GlobalException(ResultCase.NOT_ENOUGH_USER_COIN_AMOUNT);
        }

        Bid newBid;
        try {
            newBid = bidFactory.create(dto);
        } catch (Exception e) {
            coinpayService.recoverCoin(new CoinBidRequest(dto.getBidderUsername(), dto.getCoinId(), dto.getBidAmount()));
            throw new GlobalException(ResultCase.NEW_BID_CREATE_ERROR);
        }

        BidRegisterMessage bidRegisterMessage = new BidRegisterMessage(newBid.getAuctionId(), newBid.getBidderUsername(), newBid.getBidAmount());
        rabbitTemplate.convertAndSend(queueBidRegister, bidRegisterMessage);

        if (!isNewBid) {
            rabbitTemplate.convertAndSend(queueBidCancel, bidCancelMessage);
        }

        return newBid.getId();
    }

    private boolean notExceed(BigDecimal target, BigDecimal source) {
        return target.compareTo(source) <= 0 ? true : false;
    }

    @Transactional(readOnly = true)
    public List<Bid> retrieveBidPage(Pageable page) {
        return bidReader.getBidPage(page);
    }

    @Transactional(readOnly = true)
    public Bid retrieveBid(Long bidId) {
        return bidReader.getBid(bidId);
    }
}
