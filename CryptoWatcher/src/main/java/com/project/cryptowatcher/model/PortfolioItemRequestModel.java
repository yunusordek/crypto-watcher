package com.project.cryptowatcher.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PortfolioItemRequestModel implements Serializable {

    private String portfolioName;
    private String cryptoSymbol;
    private Long amount;
}
