package br.com.ovigia.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public class JwtUtil {

	private Key key;

	public JwtUtil(String secretKey) {
		this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
	}

	public Claims getAllClaimsFromToken(String token) {
		return parseToken(token).getBody();
	}

	public String getUsernameFromToken(String token) {
		return getAllClaimsFromToken(token).getSubject();
	}

	public Date getExpirationDateFromToken(String token) {
		return getAllClaimsFromToken(token).getExpiration();
	}

	private boolean isTokenExpired(String token) {
		Date expiration = null;
		try {
			expiration = getExpirationDateFromToken(token);
		} catch (Exception e) {
			return true;
		}
		return expiration.before(new Date());
	}

	public String generateToken(String email) {
		return doGenerateToken(email);
	}

	private String doGenerateToken(String email) {
		final Date expirationDate = Date.from(Instant.now().plus(Duration.ofHours(12)));

		return Jwts.builder().setSubject(email).setIssuedAt(new Date()).setExpiration(expirationDate).signWith(key)
				.compact();
	}

	public boolean isValidToken(String token) {
		return !isTokenExpired(token);
	}

	public Jws<Claims> parseToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
	}

}
