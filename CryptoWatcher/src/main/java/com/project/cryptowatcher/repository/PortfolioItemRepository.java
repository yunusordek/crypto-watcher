package com.project.cryptowatcher.repository;

import com.project.cryptowatcher.entity.PortfolioEntity;
import com.project.cryptowatcher.entity.PortfolioItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.Optional;

public interface PortfolioItemRepository extends JpaRepository<PortfolioItemEntity, Serializable> {

    Optional<PortfolioItemEntity> findByPortfolioAndCryptoSymbol(PortfolioEntity portfolio, String coinName);
}
