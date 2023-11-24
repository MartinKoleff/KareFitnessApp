package io.kare.backend.mapper;

import io.kare.backend.entity.UserEntity;
import io.kare.backend.payload.request.RegisterUserRequest;
import io.kare.backend.payload.response.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
	UserEntity map(RegisterUserRequest request, String password);
	RegisterUserResponse mapRegisterUserResponse(UserEntity entity, String token);
	LoginUserResponse mapLoginUserResponse(UserEntity entity, String token);
}
