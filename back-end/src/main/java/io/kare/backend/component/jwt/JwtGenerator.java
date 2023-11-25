package io.kare.backend.component.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtGenerator {
	private static final String SUBJECT_ID_CLAIMS_NAME = "sub";
	private final String secretKey;
	private final long expirationTime;

	public JwtGenerator(
		@Value("${jwt.secret-key}") String secretKey,
		@Value("${jwt.expiration-time}") long expirationTime
	) {
		this.secretKey = secretKey;
		this.expirationTime = expirationTime;
	}

	public String generateToken(String subjectId, Map<String, Object> claims) {
		Date creationDate = new Date();
		Date expirationDate = new Date(creationDate.getTime() + this.expirationTime);

		Map<String, Object> actualClaims = new HashMap<>(claims);
		actualClaims.put(SUBJECT_ID_CLAIMS_NAME, subjectId);

		return Jwts.builder()
			.setClaims(actualClaims)
			.setIssuedAt(creationDate)
			.setExpiration(expirationDate)
			.signWith(Keys.hmacShaKeyFor(this.secretKey.getBytes()))
			.compact();
	}

	public Claims verifyToken(String token) {
		Jws<Claims> claimsJws = Jwts.parserBuilder()
				.setSigningKey(this.secretKey.getBytes())
				.build()
				.parseClaimsJws(token);

		return claimsJws.getBody();
	}

}