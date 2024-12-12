package com.project.cryptowatcher.service;

import com.project.cryptowatcher.model.CoinModel;
import com.project.cryptowatcher.model.FavoriteCoinRequestModel;
import com.project.cryptowatcher.model.FavoriteCoinResponseModel;

import java.util.List;

public interface CryptoWatcherService {

    CoinModel getCoinDetail(String coinName);

    void addFavoriteCoin(FavoriteCoinRequestModel requestDto);

    void removeFavoriteCoin(FavoriteCoinRequestModel requestModel);

    List<FavoriteCoinResponseModel> getFavoriteCoins(Long userId);
}
