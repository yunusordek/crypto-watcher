package com.project.cryptowatcher.service;

import com.project.cryptowatcher.model.CoinModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CryptoWatcherServiceImpl implements CryptoWatcherService {

    private final CoinService coinService;

    @Override
    public CoinModel getCoinPrice(String coinName) {
        return coinService.getCoinPrice(coinName);
    }


}
