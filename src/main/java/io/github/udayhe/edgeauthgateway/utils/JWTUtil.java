package io.github.udayhe.edgeauthgateway.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author udayhegde
 */
@Service
@Slf4j
public class JWTUtil {
    //TODO This has to be removed from here and secured
    private static final String SECRET_KEY_STR = "your-very-long-and-secure-key-here-that-is-at-least-32-characters-edge-guardian";
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET_KEY_STR.getBytes(StandardCharsets.UTF_8));
    private static final long EXPIRATION_TIME = 86400000; // 24 hours in milliseconds

    public static Authentication getAuthentication(String token) {
        // Parse the token.
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Extract authorities or roles from the claims
        List<SimpleGrantedAuthority> authorities = ((List<?>) claims.get("roles")).stream()
                .map(authority -> new SimpleGrantedAuthority((String) authority))
                .collect(Collectors.toList());

        // Extract other necessary information from claims if needed, like username
        String username = claims.getSubject();

        // Return an authentication object
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static String generateToken(OidcUserInfo userInfo) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date exp = new Date(nowMillis + EXPIRATION_TIME); // Ensure EXPIRATION_TIME is defined

        return Jwts.builder()
                .setClaims(userInfo.getClaims())
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(SECRET_KEY, SignatureAlgorithm.HS512)
                .compact();
    }
}
