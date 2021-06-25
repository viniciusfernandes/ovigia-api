package br.com.ovigia.auth.security;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.ovigia.auth.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	@Value("${springbootwebfluxjjwt.jjwt.secret}")
	private String secret;

	private Key key;

	@PostConstruct
	public void init() {
		this.key = Keys.hmacShaKeyFor(secret.getBytes());
	}

	public Claims getAllClaimsFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

	public String getUsernameFromToken(String token) {
		return getAllClaimsFromToken(token).getSubject();
	}

	public Date getExpirationDateFromToken(String token) {
		return getAllClaimsFromToken(token).getExpiration();
	}

	private boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	public String generateToken(Usuario user) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("role", user.getRoles());
		return doGenerateToken(claims, user.getEmail());
	}

	public String generateToken(String email) {
		return doGenerateToken(email);
	}

	private String doGenerateToken(Map<String, Object> claims, String username) {
		final Date expirationDate = Date.from(Instant.now().plus(Duration.ofHours(12)));

		return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(new Date())
				.setExpiration(expirationDate).signWith(key).compact();
	}

	private String doGenerateToken(String email) {
		final Date expirationDate = Date.from(Instant.now().plus(Duration.ofHours(12)));

		return Jwts.builder().setSubject(email).setIssuedAt(new Date()).setExpiration(expirationDate).signWith(key)
				.compact();
	}

	public boolean validateToken(String token) {
		return !isTokenExpired(token);
	}

}
