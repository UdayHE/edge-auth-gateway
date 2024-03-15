package io.github.udayhe.edgeauthgateway.webfilter;

import io.github.udayhe.edgeauthgateway.utils.JWTUtil;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 *
 * @author udayhegde
 */
@AllArgsConstructor
public class RememberMeJwtAuthenticationFilter implements WebFilter {

    private final JWTUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .filter(authHeader -> authHeader.startsWith("Bearer "))
                .map(authHeader -> authHeader.substring(7))
                .flatMap(token -> {
                    try {
                        Claims claims = jwtUtil.parseToken(token);
                        String username = claims.getSubject();
                        // You should also extract and validate the authorities or roles from the claims
                        // For simplicity, this example assumes the user has a single 'ROLE_USER' authority
                        Authentication auth = new UsernamePasswordAuthenticationToken(
                                new User(username, "", List.of(new SimpleGrantedAuthority("ROLE_USER"))),
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_USER"))
                        );
                        return Mono.just(auth);
                    } catch (Exception e) {
                        return Mono.empty();
                    }
                })
                .flatMap(auth -> chain.filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth)))
                .switchIfEmpty(chain.filter(exchange));
    }
}
