package com.project.cryptowatcher.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioModel implements Serializable {

    private Long id;
    private String name;
    private UserModel user;
    private List<PortfolioItemModel> portfolioItems;
}
