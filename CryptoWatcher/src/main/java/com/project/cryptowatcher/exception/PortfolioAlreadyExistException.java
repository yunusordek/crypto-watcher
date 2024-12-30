package com.project.cryptowatcher.exception;

public class PortfolioAlreadyExistException extends RuntimeException {

    public PortfolioAlreadyExistException(String message) {
        super(message);
    }

}