package com.project.cryptowatcher.repository;

import com.project.cryptowatcher.entity.FavoriteCoinEntity;
import com.project.cryptowatcher.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Optional;

@Repository
public interface FavoriteCoinRepository extends JpaRepository<FavoriteCoinEntity, Serializable> {

    Optional<FavoriteCoinEntity> findByCoinSymbolAndUserEntity(String coinSymbol, UserEntity id);
}
