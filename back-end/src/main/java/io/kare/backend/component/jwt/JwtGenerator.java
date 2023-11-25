package io.kare.backend.component.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class JwtGenerator {
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

		return Jwts.builder()
			.setSubject(subjectId)
			.setClaims(claims)
			.setIssuedAt(creationDate)
			.setExpiration(expirationDate)
			.signWith(Keys.hmacShaKeyFor(this.secretKey.getBytes()))
			.compact();
	}

//	public Map.Entry<String, Map<String, Object>> verifyToken(String token) {
//
//        JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(this.secretKey.getBytes())).build();
//		var result = Map.entry(jwtParser.parseClaimsJwt(token).getBody().getSubject(),);
//		Date expirationDate = claims.getExpiration();
//		if (expirationDate.before(new Date())) {
//			throw new JwtException("Expired token");
//		}
//
//		return claims;
//	}

}