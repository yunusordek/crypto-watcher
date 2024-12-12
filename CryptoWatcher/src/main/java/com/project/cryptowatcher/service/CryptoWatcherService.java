package com.project.cryptowatcher.service;

import com.project.cryptowatcher.model.CoinModel;
import com.project.cryptowatcher.model.FavoriteCoinRequestModel;

public interface CryptoWatcherService {

    CoinModel getCoinDetail(String coinName);

    void addFavoriteCoin(FavoriteCoinRequestModel requestDto);
}
