package com.project.cryptowatcher.controller;

import com.project.cryptowatcher.model.ApiResponseDto;
import com.project.cryptowatcher.model.CoinModel;
import com.project.cryptowatcher.service.CryptoWatcherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/coins")
public class CryptoWatcherController {

    private final CryptoWatcherService cryptoWatcherService;


    @GetMapping("/{coinName}")
    public ResponseEntity<ApiResponseDto<CoinModel>> getCoin(@PathVariable String coinName) {
        return ResponseEntity.ok(createResponse(cryptoWatcherService.getCoinPrice(coinName)));
    }

    private <T> ApiResponseDto<T> createResponse(Object message) {
        ApiResponseDto<T> response = new ApiResponseDto<>();
        response.setResult(true);
        response.setMessage(message);
        return response;
    }
}
