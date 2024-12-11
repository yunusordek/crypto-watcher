package com.project.cryptowatcher.service;

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

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CryptoWatcherServiceImpl implements CryptoWatcherService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public CoinModel getCoinPrice(String coinId) {
        try {
            String priceUrl = "https://api.coingecko.com/api/v3/simple/price?ids=" + coinId + "&vs_currencies=usd";
            String coinInfoUrl = "https://api.coingecko.com/api/v3/coins/" + coinId;

            // Fiyat verisini çek
            ResponseEntity<Map<String, Map<String, Object>>> priceResponse = restTemplate.exchange(
                    priceUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );

            Map<String, Object> coinData = priceResponse.getBody().get(coinId);
            Double price = Double.valueOf((Integer) coinData.get("usd"));

            // Coin detaylarını çek
            ResponseEntity<Map<String, Object>> coinInfoResponse = restTemplate.exchange(
                    coinInfoUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );

            Map<String, Object> coinInfoData = coinInfoResponse.getBody();
            String symbol = (String) coinInfoData.get("symbol");
            String name = (String) coinInfoData.get("name");

            Map<String, Object> marketData = (Map<String, Object>) coinInfoData.get("market_data");
            Double priceChangePercentage24h = (Double) marketData.get("price_change_percentage_24h");

            return CoinModel.builder()
                    .id(coinId)
                    .symbol(symbol)
                    .name(name)
                    .price(price)
                    .changePercentage(priceChangePercentage24h)
                    .build();
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                log.error("Rate limit exceeded for CoinGecko API. CoinId: {}", coinId);
                throw new RateLimitException("Rate limit exceeded. Please try again later.");
            } else {
                log.error("CoinServiceException occurred: {}", ex.getMessage());
                throw new CoinServiceException("Sistemsel Hata Oluştu");
            }
        }
    }

}
