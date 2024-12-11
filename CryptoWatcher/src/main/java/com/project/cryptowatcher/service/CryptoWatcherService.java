package com.project.cryptowatcher.service;

import com.project.cryptowatcher.model.CoinModel;

public interface CryptoWatcherService {

    CoinModel getCoinPrice(String coinId);
}
