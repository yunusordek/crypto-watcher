package com.project.cryptowatcher.service;

import com.project.cryptowatcher.model.CoinModel;

public interface CoinService {

    CoinModel getCoinPrice(String coinName);
}