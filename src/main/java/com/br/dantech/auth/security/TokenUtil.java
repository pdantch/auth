package com.br.dantech.auth.security;

import java.security.Key;
import java.util.Collections;
import java.util.Date;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.br.dantech.auth.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

public class TokenUtil {

	private static final String ISSUER = "Dantech";
	private static final String TOKEN_HEADER = "Bearer ";
	private static final String TOKEN_KEY = "ampliarhorizontesenriqueceasarma";
	private static final long ONE_MINUTE = 60000;

	public static AuthToken encodeToken(User user) {
		Key secretKey = Keys.hmacShaKeyFor(TOKEN_KEY.getBytes());
		String tokenJWT = Jwts.builder()
				.setSubject(user.getLogin())
				.setIssuer(ISSUER)
				.setExpiration(new Date(System.currentTimeMillis() + ONE_MINUTE))
				.signWith(secretKey, SignatureAlgorithm.HS256).compact();

		return new AuthToken(TOKEN_HEADER + tokenJWT);
	}

	public static Authentication decodeToken(HttpServletRequest request) {
		try {
			String jwtToken = request.getHeader("Authorization");
			jwtToken = jwtToken.replace(TOKEN_HEADER, "");

			Jws<Claims> jwsClaims = Jwts.parserBuilder()
					.setSigningKey(TOKEN_KEY.getBytes()).build()
					.parseClaimsJws(jwtToken);

			String user = jwsClaims.getBody().getSubject();
			String issuer = jwsClaims.getBody().getIssuer();
			Date validate = jwsClaims.getBody().getExpiration();

			if (user.length() > 0 && ISSUER.equals(issuer) && validate.after(new Date(System.currentTimeMillis()))) {
				return new UsernamePasswordAuthenticationToken("user", null, Collections.emptyList());
			}

		} catch (Exception e) {
			System.out.println("ERROR ao validar token");
			System.out.println(e.getMessage());
		}

		return null;
	}
}
