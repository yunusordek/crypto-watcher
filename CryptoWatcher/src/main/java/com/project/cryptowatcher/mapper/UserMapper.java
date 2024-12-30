package com.project.cryptowatcher.mapper;

import com.project.cryptowatcher.entity.UserEntity;
import com.project.cryptowatcher.entity.UserLoginEntity;
import com.project.cryptowatcher.model.UserModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserModel entityToDto(UserEntity entity);

    UserEntity toUserEntity(UserLoginEntity userLoginEntity);
}
