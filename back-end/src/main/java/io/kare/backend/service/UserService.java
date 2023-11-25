package io.kare.backend.service;

import io.kare.backend.payload.request.*;
import io.kare.backend.payload.response.*;

public interface UserService {
	RegisterUserResponse register(RegisterUserRequest request);

	LoginUserResponse login(LoginUserRequest request);
}
