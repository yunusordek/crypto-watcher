package com.project.cryptowatcher.service;

import com.project.cryptowatcher.model.*;

import java.util.List;

public interface CryptoWatcherService {

    CoinModel getCoinDetail(String coinName);

    void addFavoriteCoin(FavoriteCoinRequestModel requestDto);

    void removeFavoriteCoin(FavoriteCoinRequestModel requestModel);

    List<FavoriteCoinResponseModel> getFavoriteCoins(String userName);

    String createPortfolio(PortfolioRequestModel requestModel);

    String addCryptoToPortfolio(PortfolioItemRequestModel requestModel);

    List<String> getUserPortfolioNames(String userName);

    PortfolioModel getPortfolio(PortfolioRequestModel requestModel);
}
