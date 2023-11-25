package io.kare.backend.exception;

public final class UserPasswordIncorrectException extends UserException {

	private static final String MESSAGE = "User password does not match! (using email %s)";
	private final String email;
	public UserPasswordIncorrectException(String email) {
		this.email = email;
	}

	@Override
	public String getMessage() {
		return MESSAGE.formatted(this.email);
	}
}
