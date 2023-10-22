package io.kare.backend.component.user;

import io.kare.backend.component.jwt.JwtGenerator;
import io.kare.backend.entity.UserEntity;
import java.util.HashMap;
import org.springframework.stereotype.Component;

@Component
public class UserJwtTokenGenerator {
	private final JwtGenerator jwtGenerator;

	public UserJwtTokenGenerator(JwtGenerator jwtGenerator) {
		this.jwtGenerator = jwtGenerator;
	}

	public String generateToken(UserEntity userEntity) {
		HashMap<String, Object> claims = new HashMap<>();
		claims.put("email", userEntity.getEmail());
		claims.put("username", userEntity.getUsername());
		claims.put("role", "USER");
		return this.jwtGenerator.generateToken(userEntity.getId(), claims);
	}
}
