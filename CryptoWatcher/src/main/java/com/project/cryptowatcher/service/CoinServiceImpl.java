package com.project.cryptowatcher.service;

import com.project.cryptowatcher.exception.CoinNotFoundException;
import com.project.cryptowatcher.exception.CoinServiceException;
import com.project.cryptowatcher.exception.RateLimitException;
import com.project.cryptowatcher.model.CoinModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CoinServiceImpl implements CoinService {
    private static final String COIN_INFO_URL = "https://api.coingecko.com/api/v3/coins/";

    @Override
    public CoinModel getCoinPrice(String coinName) {

        Map<String, Object> coinInfoData = getCoinDetail(coinName);
        Map<String, Object> marketData = getNestedMap(coinInfoData, "market_data");
        Map<String, Object> currentPrice = getNestedMap(marketData, "current_price");
        return CoinModel.builder()
                .id(coinName)
                .symbol((String) coinInfoData.get("symbol"))
                .name((String) coinInfoData.get("name"))
                .price(((Number) currentPrice.get("usd")).doubleValue())
                .changePercentage((Double) marketData.get("price_change_percentage_24h"))
                .build();

    }


    private Map<String, Object> getCoinDetail(String coinName) {
        String coinInfoUrl = COIN_INFO_URL + coinName;
        return makeApiCall(coinInfoUrl);
    }

    private Map<String, Object> makeApiCall(String url) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );
            return response.getBody();
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientErrorException(ex);
        }
    }

    private CoinServiceException handleHttpClientErrorException(HttpClientErrorException ex) {
        if (ex.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
            throw new RateLimitException("Rate limit exceeded. Please try again later.");
        } else if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new CoinNotFoundException("Coin not found");
        }
        log.error("CoinServiceException occurred: {}", ex.getMessage());
        throw new CoinServiceException("Sistemsel Hata Oluştu", ex);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getNestedMap(Map<String, Object> parent, String key) {
        return (Map<String, Object>) parent.getOrDefault(key, Collections.emptyMap());
    }
}
