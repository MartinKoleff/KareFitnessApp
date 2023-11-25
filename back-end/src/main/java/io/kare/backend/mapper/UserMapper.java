package io.kare.backend.mapper;

import io.kare.backend.entity.UserEntity;
import io.kare.backend.payload.request.RegisterUserRequest;
import io.kare.backend.payload.response.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
	@Mapping(target = "password", source = "password")
	UserEntity map(RegisterUserRequest request, String password);
	RegisterUserResponse mapRegisterUserResponse(UserEntity entity, String token);
	LoginUserResponse mapLoginUserResponse(UserEntity entity, String token);
}
