package com.project.cryptowatcher.service;

import com.project.cryptowatcher.model.PortfolioItemRequestModel;
import com.project.cryptowatcher.model.PortfolioModel;
import com.project.cryptowatcher.model.PortfolioRequestModel;

import java.util.List;

public interface PortfolioService {

    String createPortfolio(PortfolioRequestModel portfolioRequestModel);

    String addCryptoToPortfolio(PortfolioItemRequestModel portfolioItemRequestModel);

    List<String> getUserPortfolioNames(String username);

    PortfolioModel getPortfolio(PortfolioRequestModel requestModel);
}
