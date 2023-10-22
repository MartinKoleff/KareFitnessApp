package io.kare.backend.controller;

import io.kare.backend.payload.request.RegisterUserRequest;
import io.kare.backend.payload.response.RegisterUserResponse;
import io.kare.backend.service.UserService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/register")
	public ResponseEntity<RegisterUserResponse> register(@RequestBody RegisterUserRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(this.userService.register(request));
	}
}
