package com.project.cryptowatcher.service;

import java.util.Map;

public interface CoinApiClient {

    Map<String, Object> fetchCoinDetail(String coinName);
}
