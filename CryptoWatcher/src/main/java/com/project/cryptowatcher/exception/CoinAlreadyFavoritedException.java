package com.project.cryptowatcher.exception;

public class CoinAlreadyFavoritedException extends RuntimeException {

    public CoinAlreadyFavoritedException(String message) {
        super(message);
    }
}