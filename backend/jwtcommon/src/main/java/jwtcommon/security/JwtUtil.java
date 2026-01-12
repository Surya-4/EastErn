package jwtcommon.security;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private final String SECRET_KEY = "kzM8vYvpKiY1UX+mT2X8gXJ0Ty9ZZJ9GiQvIv8Ds6l8=";

	private final long expiry = 3600000;

	private final Key key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET_KEY));


	public String generateJwtToken(String userId,String userName,List<String> roles) {
		String token= Jwts.builder().
				setSubject(userName).
				claim("userId",userId).
				claim("roles", roles).
				setIssuedAt(new Date()).
				setExpiration(new Date(System.currentTimeMillis()+expiry)).
				signWith(key).compact();
	    return token;
	}
	
	public Claims extractClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}
	
	public String getUserName(String token) {
		return extractClaims(token).getSubject();
	}
	
	public String getUserId(String token) {
		return extractClaims(token).get("userId",String.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getRoles(String token) {
		return extractClaims(token).get("roles",List.class);
	}
	
	public boolean isValidToken(String token) {
		try {
			extractClaims(token);
			return true;
		}catch (JwtException | IllegalArgumentException e) {
			// TODO: handle exception
			return false;
		}
	}
}
