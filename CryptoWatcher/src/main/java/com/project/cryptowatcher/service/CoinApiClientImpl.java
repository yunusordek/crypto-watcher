package com.project.cryptowatcher.service;

import com.project.cryptowatcher.constants.ExceptionMessages;
import com.project.cryptowatcher.exception.CoinNotFoundException;
import com.project.cryptowatcher.exception.CoinServiceException;
import com.project.cryptowatcher.exception.RateLimitException;
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
@Slf4j
public class CoinApiClientImpl implements CoinApiClient {

    private static final String COIN_INFO_URL = "https://api.coingecko.com/api/v3/coins/";

    public Map<String, Object> fetchCoinDetail(String coinName) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = COIN_INFO_URL + coinName;
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );
            return response.getBody();
        } catch (HttpClientErrorException ex) {
            handleHttpClientErrorException(ex);
            throw new CoinServiceException(ExceptionMessages.UNHANDLED_EXCEPTION, ex);
        }
    }

    private void handleHttpClientErrorException(HttpClientErrorException ex) {
        if (ex.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
            throw new RateLimitException(ExceptionMessages.RATE_LIMIT_EXCEEDED);
        } else if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new CoinNotFoundException(ExceptionMessages.COIN_NOT_FOUND);
        }
        log.error("CoinApiClientException occurred: {}", ex.getMessage());
    }
}
