package com.project.cryptowatcher.controller;

import com.project.cryptowatcher.exception.*;
import com.project.cryptowatcher.model.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ApiResponseDto<Object>> buildResponse(Exception ex, HttpStatus status) {
        ApiResponseDto<Object> response = new ApiResponseDto<>();
        response.setResult(false);
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(RateLimitException.class)
    public ResponseEntity<ApiResponseDto<Object>> handleRateLimitException(RateLimitException ex) {
        return buildResponse(ex, HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(CoinServiceException.class)
    public ResponseEntity<ApiResponseDto<Object>> handleCoinServiceException(CoinServiceException ex) {
        return buildResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CoinNotFoundException.class)
    public ResponseEntity<ApiResponseDto<Object>> handleCoinNotFoundException(CoinNotFoundException ex) {
        return buildResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponseDto<Object>> handleUserNotFoundException(UserNotFoundException ex) {
        return buildResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CoinAlreadyFavoritedException.class)
    public ResponseEntity<ApiResponseDto<Object>> handleCoinAlreadyFavoritedException(CoinAlreadyFavoritedException ex) {
        return buildResponse(ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponseDto<Object>> handleUnauthorizedException(UnauthorizedException ex) {
        return buildResponse(ex, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ApiResponseDto<Object>> handleTokenExpiredException(TokenExpiredException ex) {
        return buildResponse(ex, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(TokenSignatureException.class)
    public ResponseEntity<ApiResponseDto<Object>> handleTokenSignatureException(TokenSignatureException ex) {
        return buildResponse(ex, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiResponseDto<Object>> handleInvalidTokenException(InvalidTokenException ex) {
        return buildResponse(ex, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponseDto<Object>> handleRuntimeException(RuntimeException ex) {
        return buildResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public ResponseEntity<ApiResponseDto<Object>> handleInternalServerError(HttpServerErrorException.InternalServerError ex) {
        return buildResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
