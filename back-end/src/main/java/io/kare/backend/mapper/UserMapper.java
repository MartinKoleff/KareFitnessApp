package io.kare.backend.mapper;

import io.kare.backend.entity.UserEntity;
import io.kare.backend.payload.request.RegisterUserRequest;
import io.kare.backend.payload.response.RegisterUserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
	UserEntity map(RegisterUserRequest request, String password);
	RegisterUserResponse map(UserEntity entity, String token);
}
