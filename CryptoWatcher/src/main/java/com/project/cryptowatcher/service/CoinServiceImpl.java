package com.project.cryptowatcher.service;

import com.project.cryptowatcher.constants.ExceptionMessages;
import com.project.cryptowatcher.exception.CoinServiceException;
import com.project.cryptowatcher.model.CoinModel;
import com.project.cryptowatcher.utils.MapUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class CoinServiceImpl implements CoinService {

    private final CoinApiClient coinApiClient;

    @Override
    public CoinModel getCoinDetail(String coinName) {
        Map<String, Object> coinInfoData = coinApiClient.fetchCoinDetail(coinName);

        var marketData = MapUtils.getMarketData(coinInfoData);
        if (Objects.isNull(marketData)) {
            throw new CoinServiceException(ExceptionMessages.MARKET_DATA_MISSING + coinName);
        }

        var currentPrice = MapUtils.getCurrentPrice(marketData);
        if (Objects.isNull(currentPrice) || !currentPrice.containsKey("usd")) {
            throw new CoinServiceException(ExceptionMessages.PRICE_USD_MISSING + coinName);
        }
        return CoinModel.builder()
                .id(coinName)
                .symbol((String) coinInfoData.get("symbol"))
                .name((String) coinInfoData.get("name"))
                .price(((Number) currentPrice.get("usd")).doubleValue())
                .changePercentage((Double) marketData.get("price_change_percentage_24h"))
                .build();
    }

}
