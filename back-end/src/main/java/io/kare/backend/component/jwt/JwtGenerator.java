package io.kare.backend.component.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtGenerator {

	@Value("${jwt.secret-key}")
	private String secretKey;

	@Value("${jwt.expiration-time}")
	private long expirationTime;
	public String generateToken(String subjectId, Map<String, Object> claims) {
		Date creationDate = new Date();
		Date expirationDate = new Date(creationDate.getTime() + this.expirationTime);

		return Jwts.builder()
			.setSubject(subjectId)
			.setClaims(claims)
			.setIssuedAt(creationDate)
			.setExpiration(expirationDate)
			.signWith(Keys.hmacShaKeyFor(this.secretKey.getBytes()))
			.compact();
	}
}