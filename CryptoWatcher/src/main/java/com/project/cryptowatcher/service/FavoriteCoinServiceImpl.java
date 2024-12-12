package com.project.cryptowatcher.service;

import com.project.cryptowatcher.entity.FavoriteCoinEntity;
import com.project.cryptowatcher.entity.UserEntity;
import com.project.cryptowatcher.exception.CoinAlreadyFavoritedException;
import com.project.cryptowatcher.exception.CoinServiceException;
import com.project.cryptowatcher.exception.UserNotFoundException;
import com.project.cryptowatcher.model.FavoriteCoinRequestModel;
import com.project.cryptowatcher.repository.FavoriteCoinRepository;
import com.project.cryptowatcher.repository.UserRepository;
import com.project.cryptowatcher.utils.MapUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class FavoriteCoinServiceImpl implements FavoriteCoinService {

    private final CoinApiClient coinApiClient;
    private final FavoriteCoinRepository favoriteCoinRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void addFavoriteCoin(FavoriteCoinRequestModel requestDto) {
        var user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        var coinDetail = fetchCoinDetails(requestDto.getCoinName());
        String coinSymbol = (String) coinDetail.get("symbol");

        if (favoriteCoinRepository.findByCoinSymbolAndUserEntity(coinSymbol,user).isPresent()) {
            throw new CoinAlreadyFavoritedException("The coin with symbol " + coinSymbol + " is already favorited.");
        }

        var marketData = MapUtils.getMarketData(coinDetail);
        if (Objects.isNull(marketData)) {
            throw new CoinServiceException("Market data is missing for " + requestDto.getCoinName());
        }

        var currentPrice = MapUtils.getCurrentPrice(marketData);
        if (Objects.isNull(currentPrice) || !currentPrice.containsKey("usd")) {
            throw new CoinServiceException("Current price in USD is missing for " + requestDto.getCoinName());
        }

        var favoriteCoin = createFavoriteCoinEntity(
                coinSymbol,
                (String) coinDetail.get("name"),
                ((Number) currentPrice.get("usd")).doubleValue(),
                user
        );

        favoriteCoinRepository.save(favoriteCoin);
    }

    private Map<String, Object> fetchCoinDetails(String coinName) {
        try {
            return coinApiClient.fetchCoinDetail(coinName);
        } catch (Exception e) {
            throw new CoinServiceException("Failed to fetch coin details for " + coinName, e);
        }
    }

    private FavoriteCoinEntity createFavoriteCoinEntity(String coinSymbol, String coinName, Double favoritePrice, UserEntity user) {
        return FavoriteCoinEntity.builder()
                .coinSymbol(coinSymbol)
                .coinName(coinName)
                .favoritePrice(favoritePrice)
                .userEntity(user)
                .addedAt(LocalDateTime.now())
                .build();
    }

}