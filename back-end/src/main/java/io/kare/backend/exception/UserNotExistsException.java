package io.kare.backend.exception;

public final class UserNotExistsException extends RuntimeException {

	private static final String MESSAGE = "User with email %s does not exists!";
	private final String email;

	public UserNotExistsException(String email) {
		this.email = email;
	}

	@Override
	public String getMessage() {
		return MESSAGE.formatted(this.email);
	}
}
