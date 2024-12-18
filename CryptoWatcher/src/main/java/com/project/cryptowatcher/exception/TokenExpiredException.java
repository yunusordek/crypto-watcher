package com.project.cryptowatcher.exception;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String message) {
        super(message);
    }

    public TokenExpiredException(String message, Exception e) {
        super(message, e);
    }
}