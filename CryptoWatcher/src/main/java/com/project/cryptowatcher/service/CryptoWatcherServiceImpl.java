package com.project.cryptowatcher.service;

import com.project.cryptowatcher.model.CoinModel;
import com.project.cryptowatcher.model.FavoriteCoinRequestModel;
import com.project.cryptowatcher.model.FavoriteCoinResponseModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CryptoWatcherServiceImpl implements CryptoWatcherService {

    private final CoinService coinService;
    private final FavoriteCoinService favoriteCoinService;

    @Override
    public CoinModel getCoinDetail(String coinName) {
        return coinService.getCoinDetail(coinName);
    }

    @Override
    public void addFavoriteCoin(FavoriteCoinRequestModel requestDto) {
        favoriteCoinService.addFavoriteCoin(requestDto);
    }

    @Override
    public void removeFavoriteCoin(FavoriteCoinRequestModel requestModel) {
        favoriteCoinService.removeFavoriteCoin(requestModel);
    }

    @Override
    public List<FavoriteCoinResponseModel> getFavoriteCoins(Long userId) {
        return favoriteCoinService.getFavoriteCoins(userId);
    }


}
