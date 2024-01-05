package io.kare.backend.exception_handler;

import io.kare.backend.exception.*;
import io.kare.backend.payload.response.UserErrorResponse;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class ExceptionController {

	@ExceptionHandler(UserNotExistsException.class)
	public ResponseEntity<UserErrorResponse> handle(UserAlreadyExistsException exception) {
		return this.errorOf(exception);
	}

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<UserErrorResponse> handle(UserNotExistsException exception) {
		return this.errorOf(exception);
	}

	@ExceptionHandler(UserPasswordIncorrectException.class)
	public ResponseEntity<UserErrorResponse> handle(UserPasswordIncorrectException exception) {
		return this.errorOf(exception);
	}

	@ExceptionHandler(UnauthorizedUserException.class)
	public ResponseEntity<UserErrorResponse> handle(UnauthorizedUserException exception) {
		return this.errorOf(exception);
	}

	@ExceptionHandler(WorkoutNotFoundException.class)
	public ResponseEntity<UserErrorResponse> handle(WorkoutNotFoundException exception) {
		return this.errorOf(exception);
	}

	@ExceptionHandler(WorkoutAlreadyExistsException.class)
	public ResponseEntity<UserErrorResponse> handle(WorkoutAlreadyExistsException exception) {
		return this.errorOf(exception);
	}

	@ExceptionHandler(ExerciseNotFoundException.class)
	public ResponseEntity<UserErrorResponse> handle(ExerciseNotFoundException exception) {
		return this.errorOf(exception);
	}

	@ExceptionHandler(ExerciseAlreadyExistsException.class)
	public ResponseEntity<UserErrorResponse> handle(ExerciseAlreadyExistsException exception) {
		return this.errorOf(exception);
	}

	private ResponseEntity<UserErrorResponse> errorOf(RuntimeException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UserErrorResponse(exception.getMessage()));
	}

}
