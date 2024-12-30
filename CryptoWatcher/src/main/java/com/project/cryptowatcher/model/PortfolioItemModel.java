package com.project.cryptowatcher.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioItemModel implements Serializable {

    private Long id;
    private String cryptoSymbol;
    private Long amount;
    private Double purchasePrice;
    private LocalDateTime purchaseDate;
    private Long portfolioId;
}
