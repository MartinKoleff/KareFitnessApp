package io.kare.backend.exception;

public final class UserAlreadyExistsException extends RuntimeException {
	private static final String MESSAGE = "User with email %s already exists!";
	private final String email;
	public UserAlreadyExistsException(String email) {
		this.email = email;
	}

	@Override
	public String getMessage() {
		return MESSAGE.formatted(this.email);
	}
}
