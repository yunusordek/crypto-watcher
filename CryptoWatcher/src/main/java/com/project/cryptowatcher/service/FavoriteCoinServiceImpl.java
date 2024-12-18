package com.project.cryptowatcher.service;

import com.project.cryptowatcher.constants.ExceptionMessages;
import com.project.cryptowatcher.entity.FavoriteCoinEntity;
import com.project.cryptowatcher.entity.UserEntity;
import com.project.cryptowatcher.exception.CoinAlreadyFavoritedException;
import com.project.cryptowatcher.exception.CoinNotFoundException;
import com.project.cryptowatcher.exception.CoinServiceException;
import com.project.cryptowatcher.exception.UserNotFoundException;
import com.project.cryptowatcher.mapper.FavoriteCoinMapper;
import com.project.cryptowatcher.model.FavoriteCoinRequestModel;
import com.project.cryptowatcher.model.FavoriteCoinResponseModel;
import com.project.cryptowatcher.repository.FavoriteCoinRepository;
import com.project.cryptowatcher.repository.UserRepository;
import com.project.cryptowatcher.utils.MapUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class FavoriteCoinServiceImpl implements FavoriteCoinService {

    private final CoinApiClient coinApiClient;
    private final FavoriteCoinRepository favoriteCoinRepository;
    private final UserRepository userRepository;
    private final FavoriteCoinMapper favoriteCoinMapper;

    @Override
    @Transactional
    public void addFavoriteCoin(FavoriteCoinRequestModel requestDto) {
        var user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND));

        var coinDetail = fetchCoinDetails(requestDto.getCoinName());
        String coinName = (String) coinDetail.get("name");

        if (favoriteCoinRepository.findByCoinNameAndUserEntity(coinName, user).isPresent()) {
            throw new CoinAlreadyFavoritedException(ExceptionMessages.ALL_READY_FAVORITED + coinName);
        }

        var marketData = MapUtils.getMarketData(coinDetail);
        if (Objects.isNull(marketData)) {
            throw new CoinServiceException(ExceptionMessages.MARKET_DATA_MISSING + requestDto.getCoinName());
        }

        var currentPrice = MapUtils.getCurrentPrice(marketData);
        if (Objects.isNull(currentPrice) || !currentPrice.containsKey("usd")) {
            throw new CoinServiceException(ExceptionMessages.PRICE_USD_MISSING + requestDto.getCoinName());
        }

        var favoriteCoin = createFavoriteCoinEntity(
                (String) coinDetail.get("symbol"),
                coinName,
                ((Number) currentPrice.get("usd")).doubleValue(),
                user
        );

        favoriteCoinRepository.save(favoriteCoin);
    }

    private Map<String, Object> fetchCoinDetails(String coinName) {
        try {
            return coinApiClient.fetchCoinDetail(coinName);
        } catch (Exception e) {
            throw new CoinServiceException(ExceptionMessages.COIN_FETCH_FAILED + coinName, e);
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

    @Override
    public void removeFavoriteCoin(FavoriteCoinRequestModel requestModel) {
        var user = userRepository.findById(requestModel.getUserId())
                .orElseThrow(() -> new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND));
        var coin =
                favoriteCoinRepository.findByCoinNameAndUserEntity(
                                requestModel.getCoinName(), user)
                        .orElseThrow(() -> new CoinNotFoundException(ExceptionMessages.COIN_NOT_FOUND));

        favoriteCoinRepository.delete(coin);
    }

    @Override
    public List<FavoriteCoinResponseModel> getFavoriteCoins(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND));
        var favoriteCoins = favoriteCoinRepository.findByUserEntity(user);
        return favoriteCoinMapper.entityToDto(favoriteCoins);
    }
}
