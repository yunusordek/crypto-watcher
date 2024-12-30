package com.project.cryptowatcher.repository;

import com.project.cryptowatcher.entity.PortfolioEntity;
import com.project.cryptowatcher.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<PortfolioEntity, Serializable> {

    Optional<PortfolioEntity> findByName(String portfolioName);

    List<PortfolioEntity> findByUser(UserEntity user);

    Optional<PortfolioEntity> findByNameAndUser(String portfolioName, UserEntity user);

}
