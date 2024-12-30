package com.project.cryptowatcher.mapper;

import com.project.cryptowatcher.entity.PortfolioEntity;
import com.project.cryptowatcher.model.PortfolioModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { PortfolioItemMapper.class })
public interface PortfolioMapper {

    @Mapping(source = "items", target = "portfolioItems")
    PortfolioModel entityToDto(PortfolioEntity entity);

    @Mapping(source = "portfolioItems", target = "items")
    PortfolioEntity dtoToEntity(PortfolioModel dto);
}
