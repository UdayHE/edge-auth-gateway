package io.github.udayhe.edgeauthgateway.oidc;

import io.github.udayhe.edgeauthgateway.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 *
 * @author udayhegde
 */
@Slf4j
public class CustomAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        return exchange.getSession().flatMap(session -> {
            String originalUrl = exchange.getRequest().getURI().toString();
            session.getAttributes().put(Constants.ORIGINAL_REQUEST_URL, originalUrl);
            String springSecurityUrl = (String) session.getAttributes().get(Constants.SPRING_SECURITY_SAVED_REQUEST);
            if (log.isDebugEnabled()) {
                log.debug("Original URL saved in session: {}", originalUrl);
                log.debug("Spring Security saved request URL in session: {}", springSecurityUrl);
            }

            if (springSecurityUrl != null) {
                exchange.getResponse().setStatusCode(HttpStatus.FOUND);
                exchange.getResponse().getHeaders().setLocation(URI.create(springSecurityUrl));
                return exchange.getResponse().setComplete();
            } else {
                log.error("No Redirection Url found");
                return Mono.empty();
            }
        });
    }
}
