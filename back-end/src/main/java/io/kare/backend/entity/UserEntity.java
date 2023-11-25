package io.kare.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = UserEntity.TABLE_NAME)
public class UserEntity {
	public static final String TABLE_NAME = "users";
	public static final String ID_COLUMN = "id";
	public static final String EMAIL_COLUMN = "email";
	public static final String USERNAME_COLUMN = "username";
	public static final String PASSWORD_COLUMN = "password";

	private String id;
	private String email;
	private String username;
	private String password;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = ID_COLUMN, updatable = false, unique = true, nullable = false)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = EMAIL_COLUMN, unique = true, nullable = false)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = USERNAME_COLUMN, nullable = false)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = PASSWORD_COLUMN, nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
