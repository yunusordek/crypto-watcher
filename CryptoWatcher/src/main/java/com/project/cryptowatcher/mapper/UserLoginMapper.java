package com.project.cryptowatcher.mapper;

import com.project.cryptowatcher.entity.UserLoginEntity;
import com.project.cryptowatcher.model.RegisterRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserLoginMapper {

    @Mapping(target = "password", source = "encodedPassword")
    UserLoginEntity toUserLoginEntity(RegisterRequestModel registerRequest, String encodedPassword);
}