package com.project.cryptowatcher.exception;

public class CoinNotFoundException extends RuntimeException{
    public CoinNotFoundException(String message){
        super(message);
    }

}