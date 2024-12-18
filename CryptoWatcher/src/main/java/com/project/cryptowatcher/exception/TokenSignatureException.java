package com.project.cryptowatcher.exception;

public class TokenSignatureException extends RuntimeException {
    public TokenSignatureException(String message) {
        super(message);
    }

    public TokenSignatureException(String message, Exception e) {
        super(message, e);
    }
}