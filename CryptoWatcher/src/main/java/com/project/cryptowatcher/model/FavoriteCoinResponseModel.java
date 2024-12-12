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
public class FavoriteCoinResponseModel implements Serializable {
    private String coinSymbol;
    private String coinName;
    private LocalDateTime addedAt;
    private Double favoritePrice;
}
