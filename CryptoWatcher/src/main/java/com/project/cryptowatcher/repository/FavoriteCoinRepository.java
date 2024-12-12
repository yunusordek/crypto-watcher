package com.project.cryptowatcher.repository;

import com.project.cryptowatcher.entity.FavoriteCoinEntity;
import com.project.cryptowatcher.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteCoinRepository extends JpaRepository<FavoriteCoinEntity, Serializable> {

    Optional<FavoriteCoinEntity> findByCoinNameAndUserEntity(String coinName, UserEntity id);

    List<FavoriteCoinEntity> findByUserEntity(UserEntity id);
}
