package com.project.cryptowatcher.controller;

import com.project.cryptowatcher.model.ApiResponseDto;
import com.project.cryptowatcher.model.CoinModel;
import com.project.cryptowatcher.model.FavoriteCoinRequestModel;
import com.project.cryptowatcher.model.FavoriteCoinResponseModel;
import com.project.cryptowatcher.service.CryptoWatcherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/coins")
public class CryptoWatcherController {

    private final CryptoWatcherService cryptoWatcherService;


    @GetMapping("/{coinName}")
    public ResponseEntity<ApiResponseDto<CoinModel>> getCoinDetail(@PathVariable String coinName) {
        return ResponseEntity.ok(createResponse(cryptoWatcherService.getCoinDetail(coinName)));
    }

    @PostMapping("/addFavoriteCoin")
    public ResponseEntity<ApiResponseDto> addFavoriteCoin(@RequestBody FavoriteCoinRequestModel requestDto) {
        cryptoWatcherService.addFavoriteCoin(requestDto);
        return ResponseEntity.ok(createResponse("Favorite coin added successfully"));
    }

    @DeleteMapping("/deleteFavoriteCoin")
    public ResponseEntity<ApiResponseDto> removeFavoriteCoin(@RequestBody FavoriteCoinRequestModel requestDto) {
        cryptoWatcherService.removeFavoriteCoin(requestDto);
        return ResponseEntity.ok(createResponse("Favorite coin removed successfully"));
    }

    @GetMapping("/favoriteCoins/{userId}")
    public ResponseEntity
            <ApiResponseDto<List<FavoriteCoinResponseModel>>> getFavoriteCoins(@PathVariable Long userId) {
        return ResponseEntity.ok(createResponseList(cryptoWatcherService.getFavoriteCoins(userId)));
    }

    private <T> ApiResponseDto<T> createResponse(Object message) {
        ApiResponseDto<T> response = new ApiResponseDto<>();
        response.setResult(true);
        response.setMessage(message);
        return response;
    }

    private <T> ApiResponseDto<List<T>> createResponseList(List<T> message) {
        ApiResponseDto<List<T>> response = new ApiResponseDto<>();
        response.setResult(true);
        response.setMessage(message);
        return response;
    }
}
