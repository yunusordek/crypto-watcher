package com.project.cryptowatcher.controller;

import com.project.cryptowatcher.model.*;
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

    @GetMapping("/favoriteCoins/{userName}")
    public ResponseEntity
            <ApiResponseDto<List<FavoriteCoinResponseModel>>> getFavoriteCoins(@PathVariable String userName) {
        return ResponseEntity.ok(createResponseList(cryptoWatcherService.getFavoriteCoins(userName)));
    }

    @PostMapping("/createPortfolio")
    public ResponseEntity<ApiResponseDto> createPortfolio(@RequestBody PortfolioRequestModel requestDto) {
        return ResponseEntity.ok(createResponse(cryptoWatcherService.createPortfolio(requestDto)));
    }

    @PostMapping("/addCryptoToPortfolio")
    public ResponseEntity<ApiResponseDto> addCryptoToPortfolio(@RequestBody PortfolioItemRequestModel requestDto) {
        return ResponseEntity.ok(createResponse(cryptoWatcherService.addCryptoToPortfolio(requestDto)));
    }

    @GetMapping("/getUserPortfolioNames/{username}")
    public ResponseEntity<ApiResponseDto> getUserPortfolioNames(@PathVariable String username) {
        return ResponseEntity.ok(createResponse(cryptoWatcherService.getUserPortfolioNames(username)));
    }

    @PostMapping("/getPortfolio")
    public ResponseEntity<ApiResponseDto> getPortfolio(@RequestBody PortfolioRequestModel requestDto) {
        return ResponseEntity.ok(createResponse(cryptoWatcherService.getPortfolio(requestDto)));
    }

    private <T> ApiResponseDto<T> createResponse(Object data) {
        ApiResponseDto<T> response = new ApiResponseDto<>();
        response.setResult(true);
        response.setData(data);
        return response;
    }

    private <T> ApiResponseDto<List<T>> createResponseList(List<T> data) {
        ApiResponseDto<List<T>> response = new ApiResponseDto<>();
        response.setResult(true);
        response.setData(data);
        return response;
    }
}
