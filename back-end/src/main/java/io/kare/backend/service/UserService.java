package io.kare.backend.service;

import io.kare.backend.payload.request.RegisterUserRequest;
import io.kare.backend.payload.response.RegisterUserResponse;

public interface UserService {
	RegisterUserResponse register(RegisterUserRequest request);
}
