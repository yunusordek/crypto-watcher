package com.project.cryptowatcher.service;

import com.project.cryptowatcher.model.FavoriteCoinRequestModel;
import com.project.cryptowatcher.model.FavoriteCoinResponseModel;

import java.util.List;

public interface FavoriteCoinService {

    void addFavoriteCoin(FavoriteCoinRequestModel requestDto);

    void removeFavoriteCoin(FavoriteCoinRequestModel requestModel);

    List<FavoriteCoinResponseModel> getFavoriteCoins(String userName);
}
