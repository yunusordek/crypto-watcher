package com.project.cryptowatcher.mapper;

import com.project.cryptowatcher.entity.FavoriteCoinEntity;
import com.project.cryptowatcher.model.FavoriteCoinResponseModel;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FavoriteCoinMapper {

    List<FavoriteCoinResponseModel> entityToDto(List<FavoriteCoinEntity> favoriteCoinEntity);
}
