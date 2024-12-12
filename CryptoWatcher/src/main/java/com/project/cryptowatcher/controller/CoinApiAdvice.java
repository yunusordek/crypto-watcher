package com.project.cryptowatcher.controller;

import com.project.cryptowatcher.exception.*;
import com.project.cryptowatcher.model.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CoinApiAdvice {
    @ExceptionHandler(RateLimitException.class)
    public ResponseEntity<ApiResponseDto<Object>> handleRateLimitException(RateLimitException ex) {
        ApiResponseDto<Object> response = new ApiResponseDto<>();
        response.setResult(false);
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(CoinServiceException.class)
    public ResponseEntity<ApiResponseDto<Object>> handleCoinServiceException(CoinServiceException ex) {
        ApiResponseDto<Object> response = new ApiResponseDto<>();
        response.setResult(false);
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CoinNotFoundException.class)
    public ResponseEntity<ApiResponseDto<Object>> handleCoinNotFoundException(CoinNotFoundException ex) {
        ApiResponseDto<Object> response = new ApiResponseDto<>();
        response.setResult(false);
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponseDto<Object>> handleUserNotFoundException(UserNotFoundException ex) {
        ApiResponseDto<Object> response = new ApiResponseDto<>();
        response.setResult(false);
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CoinAlreadyFavoritedException.class)
    public ResponseEntity<ApiResponseDto<Object>> handleCoinAlreadyFavoritedException(CoinAlreadyFavoritedException ex) {
        ApiResponseDto<Object> response = new ApiResponseDto<>();
        response.setResult(false);
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
}
