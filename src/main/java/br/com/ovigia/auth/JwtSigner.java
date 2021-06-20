package br.com.ovigia.auth;

import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import br.com.ovigia.businessrule.exception.AutenticacaoException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SigningKeyResolver;
import io.jsonwebtoken.SigningKeyResolverAdapter;
import io.jsonwebtoken.security.Keys;

public class JwtSigner {
	private static final java.security.KeyPair KeyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);
	private static final String secret = "iqLuM5a68y7xM2ZOmcIXyXQFE8vgs1uKsCWtm2DkqE4=";
	private SigningKeyResolver signingKeyResolver = new SigningKeyResolverAdapter() {
		@Override
		public byte[] resolveSigningKeyBytes(JwsHeader header, Claims claims) {
			return Base64.getDecoder().decode(secret);
		}
	};

	public String createJwt(String userId) {
		/*
		 * return
		 * Jwts.builder().setIssuer("ovigia").setId(userId).setSubject("msilverman")
		 * .claim("name", "Micah Silverman").claim("scope", "admins") // Fri Jun 24 2016
		 * 15:33:42 GMT-0400 (EDT)
		 * .setIssuedAt(Date.from(Instant.ofEpochSecond(1466796822L))) // Sat Jun 24
		 * 2116 15:33:42 GMT-0400 (EDT)
		 * .setExpiration(Date.from(Instant.ofEpochSecond(4622470422L))) .signWith(
		 * SignatureAlgorithm.HS256,
		 * Base64.getDecoder().decode("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E="))
		 * .compact();
		 */
		return Jwts.builder().signWith(SignatureAlgorithm.HS256, Base64.getDecoder().decode(secret)).setSubject(userId)
				.setIssuer("ovigia").setExpiration(Date.from(Instant.now().plus(Duration.ofHours(12))))
				.setIssuedAt(Date.from(Instant.now())).compact();
	}

	/**
	 * Validate the JWT where it will throw an exception if it isn't valid.
	 */
	public Jws<Claims> parseJwt(String jwt) {
		// return
		// Jwts.parserBuilder().setSigningKey(KeyPair.getPublic()).build().parseClaimsJws(jwt);

		try {
			Jws<Claims> jws = Jwts.parser().setSigningKeyResolver(signingKeyResolver).parseClaimsJws(jwt);
			return jws;
		} catch (Exception e) {
			throw new AutenticacaoException();
		}

	}

	public boolean isExpired(String jwt) {

		try {
			return parseJwt(jwt).getBody().getExpiration().after(Date.from(Instant.now().plus(Duration.ofHours(12))));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static void main(String[] args) {
		 System.out.println(new JwtSigner().createJwt("viniciussf@hotmail.com"));
	}

}