package com.project.cryptowatcher.exception;

public class CoinServiceException extends RuntimeException {
    public CoinServiceException(String message) {
        super(message);
    }

    public CoinServiceException(String message, Exception e) {
        super(message, e);
    }


}
