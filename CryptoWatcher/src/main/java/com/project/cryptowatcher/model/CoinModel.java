package com.project.cryptowatcher.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoinModel implements Serializable {
    private String id;
    private String symbol;
    private String name;
    private Double price;
    private Double changePercentage;

}