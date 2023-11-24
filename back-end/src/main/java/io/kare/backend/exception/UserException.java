package io.kare.backend.exception;

public abstract sealed class UserException extends RuntimeException permits
	UserAlreadyExistsException, UserNotExistsException,
	UserPasswordIncorrectException {

}
