package com.resumeradar.config;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtils {
	@Value("${secret_key}")
	private String SECRET_KEY;

	private SecretKey getSigningKey() {
		return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
	}

	public String generateToken(String userid) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("userid", userid);
		return createToken(claims, "loginuser");
	}

	private String createToken(Map<String, Object> claims, String subject) {
		Date createDate = new Date(System.currentTimeMillis());
		Date expireDate = new Date(System.currentTimeMillis() + 1000 * 60 * 100);

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(createDate).setExpiration(expireDate) // 100
																														// minute
																														// validity
				.signWith(getSigningKey()).compact();
	}

	public String extractUserID(String token) {
		final Claims claims = extractAllClaims(token);
		String id = claims.get("userid", String.class);
		return id;
	}

	public Boolean isTokenExpired(String token) {
		try {
			return extractExpiration(token).before(new Date());
		} catch (ExpiredJwtException e) {
			return true;
		}
	}

	private Date extractExpiration(String token) {
		final Claims claims = extractAllClaims(token);
		return claims.getExpiration();
	}

	public Claims extractAllClaims(String token) {
		try {
			return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
		} catch (ExpiredJwtException e) {
			throw e;
		} catch (JwtException e) {
			throw new SecurityException("Invalid or tampered JWT token", e);
		}
	}

	public boolean validateToken(String token) {
		try {
			extractAllClaims(token); // will throw if invalid
			return true;
		} catch (ExpiredJwtException e) {
			throw e;
		} catch (JwtException e) {
			return false;
		}
	}
}
