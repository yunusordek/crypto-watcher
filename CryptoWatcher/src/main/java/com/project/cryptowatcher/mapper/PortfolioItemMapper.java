package com.project.cryptowatcher.mapper;

import com.project.cryptowatcher.entity.PortfolioItemEntity;
import com.project.cryptowatcher.model.PortfolioItemModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PortfolioItemMapper {
    @Mapping(target = "portfolioId", source = "portfolio.id")
    PortfolioItemModel entityToDto(PortfolioItemEntity entity);

    @Mapping(target = "portfolio.id", source = "portfolioId")
    PortfolioItemEntity dtoToEntity(PortfolioItemModel dto);
}
