package com.project.cryptowatcher.service;

import com.project.cryptowatcher.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CryptoWatcherServiceImpl implements CryptoWatcherService {

    private final CoinService coinService;
    private final FavoriteCoinService favoriteCoinService;
    private final PortfolioService portfolioService;

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
    public List<FavoriteCoinResponseModel> getFavoriteCoins(String userName) {
        return favoriteCoinService.getFavoriteCoins(userName);
    }

    @Override
    public String createPortfolio(PortfolioRequestModel requestModel) {
        return portfolioService.createPortfolio(requestModel);
    }

    @Override
    public String addCryptoToPortfolio(PortfolioItemRequestModel requestModel) {
        return portfolioService.addCryptoToPortfolio(requestModel);
    }

    @Override
    public List<String> getUserPortfolioNames(String username) {
        return portfolioService.getUserPortfolioNames(username);
    }

    @Override
    public PortfolioModel getPortfolio(PortfolioRequestModel requestModel) {
        return portfolioService.getPortfolio(requestModel);
    }
}
