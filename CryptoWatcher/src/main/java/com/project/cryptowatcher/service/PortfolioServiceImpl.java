package com.project.cryptowatcher.service;

import com.project.cryptowatcher.constants.ExceptionMessages;
import com.project.cryptowatcher.entity.PortfolioEntity;
import com.project.cryptowatcher.exception.CoinNotFoundException;
import com.project.cryptowatcher.exception.PortfolioAlreadyExistException;
import com.project.cryptowatcher.exception.PortfolioNotFoundException;
import com.project.cryptowatcher.exception.UserNotFoundException;
import com.project.cryptowatcher.mapper.PortfolioItemMapper;
import com.project.cryptowatcher.mapper.PortfolioMapper;
import com.project.cryptowatcher.mapper.UserMapper;
import com.project.cryptowatcher.model.PortfolioItemModel;
import com.project.cryptowatcher.model.PortfolioItemRequestModel;
import com.project.cryptowatcher.model.PortfolioModel;
import com.project.cryptowatcher.model.PortfolioRequestModel;
import com.project.cryptowatcher.repository.PortfolioItemRepository;
import com.project.cryptowatcher.repository.PortfolioRepository;
import com.project.cryptowatcher.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {

    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;
    private final UserMapper userMapper;
    private final PortfolioMapper portfolioMapper;
    private final CoinService coinService;
    private final PortfolioItemMapper portfolioItemMapper;
    private final PortfolioItemRepository portfolioItemRepository;


    @Transactional
    public String createPortfolio(PortfolioRequestModel portfolioRequestModel) {
        var userEntity = userRepository.findByUsername(portfolioRequestModel.getUsername())
                .orElseThrow(() -> new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND));
        var portfolioModel = PortfolioModel.builder()
                .name(portfolioRequestModel.getPortfolioName())
                .user(userMapper.entityToDto(userEntity))
                .build();
        var portfolioEntity = portfolioMapper.dtoToEntity(portfolioModel);
        var existingPortfolio = portfolioRepository.findByName(portfolioModel.getName());
        if (existingPortfolio.isPresent()) {
            throw new PortfolioAlreadyExistException(ExceptionMessages.PORTFOLIO_ALREADY_EXISTS);
        }

        portfolioRepository.save(portfolioEntity);
        return "Portfolio successfully created.";
    }

    @Transactional
    @Override
    public String addCryptoToPortfolio(PortfolioItemRequestModel requestModel) {
        var portfolio = portfolioRepository.findByName(requestModel.getPortfolioName())
                .orElseThrow(() -> new PortfolioNotFoundException(ExceptionMessages.PORTFOLIO_NOT_FOUND));

        var coinInfo = coinService.getCoinDetail(requestModel.getCryptoSymbol());
        if (Objects.isNull(coinInfo)) {
            throw new CoinNotFoundException(ExceptionMessages.COIN_NOT_FOUND);
        }

        var existingPortfolio = portfolioItemRepository.findByPortfolioAndCryptoSymbol(portfolio, coinInfo.getSymbol());
        if (existingPortfolio.isPresent()) {
            var portfolioItem = existingPortfolio.get();
            portfolioItem.setAmount(portfolioItem.getAmount() + requestModel.getAmount());
            portfolioItem.setPurchasePrice((portfolioItem.getPurchasePrice() + coinInfo.getPrice()) / 2);
            portfolioItemRepository.save(portfolioItem);
            return "Portfolio updated";
        }

        var model = PortfolioItemModel.builder()
                .cryptoSymbol(coinInfo.getSymbol())
                .amount(requestModel.getAmount())
                .purchasePrice(coinInfo.getPrice())
                .purchaseDate(LocalDateTime.now())
                .portfolioId(portfolio.getId())
                .build();

        portfolioItemRepository.save(portfolioItemMapper.dtoToEntity(model));
        return "Portfolio added";
    }

    @Override
    public List<String> getUserPortfolioNames(String username) {
        var userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND));
        var portfolioList = portfolioRepository.findByUser(userEntity);
        return portfolioList.stream().map(PortfolioEntity::getName).collect(Collectors.toList());
    }

    @Override
    public PortfolioModel getPortfolio(PortfolioRequestModel requestModel) {
        var userEntity = userRepository.findByUsername(requestModel.getUsername())
                .orElseThrow(() -> new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND));
        var portfolio = portfolioRepository.findByNameAndUser(requestModel.getPortfolioName(), userEntity)
                .orElseThrow(() -> new PortfolioNotFoundException(ExceptionMessages.PORTFOLIO_NOT_FOUND));
        return portfolioMapper.entityToDto(portfolio);
    }

}
