package io.kare.backend.exception_handler;

import io.kare.backend.exception.UserAlreadyExistsException;
import io.kare.backend.payload.response.UserErrorResponse;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class UserExceptionHandler {

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<UserErrorResponse> handle(UserAlreadyExistsException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UserErrorResponse(exception.getMessage()));
	}

}
